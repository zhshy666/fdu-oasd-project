<template>
<div>
<div class="layui-container">
    <div style="height: 150px"></div>
</div>
  <el-container>
    <el-header height="50px">
      <div class="layui-col-md4 layui-col-md-offset4">
        <h1>Login</h1><br>
        <p style="font-size:small">
          <span>Into the world of 
            <el-link @click="goToIndex" :underline="false" style="font-size:small">Travellers</el-link>
          </span>
        </p>
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
          <el-form-item prop="usernameOrEmail" >
            <el-input
                type="text"
                v-model="loginForm.usernameOrEmail"
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
              <div style="font-size:small">
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
            <el-button
              type="primary"
              native-type="submit"
              :disabled="isDisabled"
              size="medium"
              style="width:100%"
              v-on:click="login">
                Login
            </el-button>
          </el-form-item>

          <el-form-item size="medium">
            <el-button 
              style="width:100%"
              plain
              @click="resetForm('loginForm')">
                Reset
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <div class="layui-col-md4 layui-col-md-offset4">
        <p style="font-size:small"><span>Don't have an account yet? <el-link @click="register" :underline="false" style="font-size:small">Register one.</el-link></span></p>
      </div>
    </el-main>
  </el-container>
</div>
</template>

<script>
import SIdentify from '../components/RandomCode'
import {Encrypt} from '../tools/AESUtils'
  export default {
    name: "Login",
    components: {SIdentify},
    data() {
      return {
        identifyCodes: '1234567890',
				identifyCode: '',
        loginForm: {
          usernameOrEmail: "",
          password: "",
          verifycode: '',
        },
        rules: {
          usernameOrEmail: [
            {required:true,message:"Username / Email is required",blur:"change"},
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
        return !(this.loginForm.usernameOrEmail && this.loginForm.password
        && this.loginForm.verifycode === this.identifyCode
        );
      }
    },
    methods: {
      goToIndex(){
        this.$router.replace({ path: "/" });
      },
      login() {
        // Turn to loading mode when the form is submitted,and come back when getting response
        this.loading = true;
        let passwordAES = Encrypt(this.loginForm.password.trim()).toString();
        this.$axios
          .post("/login", {
            usernameOrEmail: this.loginForm.usernameOrEmail,
            password: passwordAES
          })
          .then(resp => {
            if (resp.status === 200 && resp.data.hasOwnProperty("token")) {
              //Save token
              this.$store.commit("login", resp.data);
              this.$notify({
                dangerouslyUseHTMLString: true,
                type:'success',
                title: 'Login success',
                message: '<strong style="color:teal">'+this.$store.state.cur_user+', welcome back</strong>',
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
      register(){
        this.$router.replace({path: "/register"})
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      errorNotification(){
        this.$notify({
          type:'error',
          dangerouslyUseHTMLString: true,
          title: 'Login error',
          message: '<strong style="color:teal">Please check your username / email and password or try again later!</strong>'
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
			}
    }
  }
</script>

<style scope>
.mySpan{
  color: #009688;
  font-weight: bold;
}
</style>