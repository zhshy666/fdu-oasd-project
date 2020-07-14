<template>
<div>
<div class="layui-container">
    <div style="height: 150px"></div>
</div>
    <el-container>
        <el-header height="50%">
          <div class="layui-col-md4 layui-col-md-offset4">
              <h1>Login</h1>
          </div>
        </el-header>
        <el-main>
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
                        <button v-if="isDisabled"
                          type="button"
                          class="layui-btn layui-btn-disabled"
                          native-type="submit"
                          disabled
                          size="medium"
                          style="width:100% "
                          v-on:click="login">
                            Login
                        </button>
                        <button v-else
                          type="button"
                          class="layui-btn"
                          native-type="submit"
                          size="medium"
                          style="width:100% "
                          v-on:click="login">
                            Login
                        </button>
                    </el-form-item>
                    <el-form-item size="medium">
                        <button 
                          type="button"
                          style="width:100%"
                          @click="resetForm('loginForm')"
                          class="layui-btn layui-btn-primary">
                            Reset
                        </button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-main>
    </el-container>
</div>
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
          ],
          password: [
            {required:true, message:"Password is required", blur:"change"},
          ]
        },
        loading: false,
      };
    },
    computed:{
      isDisabled(){
        return !(this.loginForm.username && this.loginForm.password);
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
              this.$notify({
                dangerouslyUseHTMLString: true,
                type:'success',
                title: 'Login success',
                showClose: false,
                message: '<strong style="color:teal"Login successfully!</strong>',
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
          showClose: false,
          title: 'Login error',
          message: '<strong style="color:teal">Please check your username and password or try again later!</strong>'
        });
      },
    }
  }
</script>
