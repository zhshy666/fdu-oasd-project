<template>
    <el-container>
        <el-header>
            Login
        </el-header>
        <el-main>
            <el-row :gutter="20">
                <el-col :span="6" :offset="9">
                    <el-form 
                    @submit.native.prevent
                    status-icon
                    :model="loginForm"
                    :rules="rules"
                    ref="loginForm"
                    label-position="left"
                    label-width="0px"
                    v-loading="loading"
                    >
                        <el-form-item prop="username" >
                            <el-input
                                type="text"
                                v-model="loginForm.username"
                                auto-complete="off"
                                placeholder="Username"
                            ></el-input>
                        </el-form-item>
                        <el-form-item prop="password" >
                            <el-input
                                type="password"
                                show-password
                                v-model="loginForm.password"
                                auto-complete="off"
                                placeholder="Password"
                            ></el-input>
                        </el-form-item>
                            
                        <el-form-item size="medium">
                            <el-button
                            native-type="submit"
                            :disabled ="isDisabled"
                            size="medium"
                            type="primary"
                            style="width:100% "
                            v-on:click="login"
                            >Sign In</el-button>
                        </el-form-item>
                        <el-form-item size="medium">
                            <el-button
                            plain
                            style="width:100%"
                            @click="resetForm('loginForm')"
                            >Reset
                            </el-button>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
  export default {
    name: "Login",
    data() {
      return {
        loginForm: {
          username: "",
          password: ""
        },
        rules: {
          username: [
            {required:true,message:"Username is required",blur:"change"},
            {pattern:/^[a-zA-Z-][a-zA-Z0-9-_]{4,31}$/,message:"Invalid username",blur:"change"}
          ],
          password: [
            {required:true, message:"Password is required", blur:"change"},
            {pattern:/^[\w-]{6,32}$/, message:"Invalid password",blur:"change"},
          ]
        },
        loading: false,
      };
    },
    computed:{
      isDisabled(){
        return !(/^[a-zA-Z-][a-zA-Z0-9-_]{4,31}$/.test(this.loginForm.username) && /^[\w-]{6,32}$/.test(this.loginForm.password));
      }
    },
    methods: {
      login() {
        // Turn to loading mode when the form is submitted,and come back when getting response
        this.loading = true;
        this.$axios
          .post("/login", {
            username: this.loginForm.username,
            password: this.loginForm.password
          })
          .then(resp => {
            if (resp.status === 200 && resp.data.hasOwnProperty("token")) {
              //Save token
              this.$store.commit("login", resp.data);
              this.$message({
                dangerouslyUseHTMLString: true,
                type:'success',
                message: '<strong style="color:teal">Welcome back!</strong>',
                center:true
              });
              this.$router.replace({ path: "/" });
            } else {
              this.errorNotification();
              this.loading = false;
            }
          })
          .catch(error => {
            console.log(error);
            this.errorNotification();
            this.loading = false;
          });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      errorNotification(){
        this.$notify({
          type:'error',
          dangerouslyUseHTMLString: true,
          title: 'Login error',
          message: '<strong style="color:teal">Please check your username and password or try again later!</strong>'
        });
      },
    }
  }
</script>
