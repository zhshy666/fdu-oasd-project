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
                placeholder="Username / Email"
                prefix-icon="el-icon-user"
              ></el-input>
          </el-form-item>

          <el-form-item prop="password" >
            <el-input
              type="password"
              show-password
              v-model="loginForm.password"
              auto-complete="off"
              prefix-icon="el-icon-lock"
              placeholder="Password"
            ></el-input>
          </el-form-item>

          <el-form-item prop="verifycode">
            <el-popover
              placement="right"
              width="220"
              trigger="focus"
            >
              <div>
                <p>· <span class="mySpan">Click</span> to change the verify code</p>
                <p @click="refreshCode">
                  · <s-identify :identifyCode="identifyCode"></s-identify>
                </p>
              </div>
              <el-input 
                type="text"
                slot="reference"
                auto-complete="off"
                v-model="loginForm.verifycode" 
                prefix-icon="el-icon-key"
                placeholder="Verify code">
              </el-input>
            </el-popover>
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
import SIdentify from '../components/RandomCode'
  export default {
    name: "Login",
    components: {SIdentify},
    data() {
      return {
        identifyCodes: '1234567890',
				identifyCode: '',
        loginForm: {
          username: "",
          password: "",
          verifycode: '',
        },
        rules: {
          username: [
            {required:true,message:"Username is required",blur:"change"},
          ],
          password: [
            {required:true, message:"Password is required", blur:"change"},
          ],
          verifycode:[
            {required: true, message: "Verify code is required", blur: "change"},
            {
              validator:(rule,value,callback)=>{
                if(value !== this.identifyCode) {
                  callback(new Error('Wrong verify code'));
                }
                callback();
              },
              message:"Wrong verify code",
              trigger:"change"
            }
          ],
        },
        loading: false,
      };
    },
    mounted() {
			// init
			this.identifyCode = ''
			this.makeCode(this.identifyCodes, 4)
		},
    computed:{
      isDisabled(){
        return !(this.loginForm.username && this.loginForm.password
        && this.loginForm.verifycode === this.identifyCode
        );
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
      randomNum(min, max) {
				return Math.floor(Math.random() * (max - min) + min)
			},
			// refresh verify code
			refreshCode() {
				this.identifyCode = ''
				this.makeCode(this.identifyCodes, 4)
			},
			// get random verify code
			makeCode(o, l) {
				for (let i = 0; i < l; i++) {
					this.identifyCode += this.identifyCodes[this.randomNum(0, this.identifyCodes.length)]
				}
				console.log(this.identifyCode)
			}
    }
  }
</script>

<style>
.mySpan{
  color: #009688;
  font-weight: bold;
}
</style>