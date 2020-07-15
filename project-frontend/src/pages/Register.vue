<template>
<div>
<div class="layui-container">
    <div style="height: 60px"></div>
</div>
  <el-container>
    <el-header height="50%">
      <div class="layui-col-md4 layui-col-md-offset4">
        <h1>Register</h1>
      </div>
    </el-header>
    <el-main>
      <el-col :span="6" :offset="9">
        <el-form 
          @submit.native.prevent
          status-icon
          :model="registerForm"
          :rules="rules"
          ref="registerForm"
          label-position="left"
          label-width="0px"
          v-loading="loading"
        >
          <el-form-item prop="username" >
            <el-popover
              placement="right"
              width="200"
              trigger="focus"
            >
              <div style="font-size:small">
                <p>· <span class="mySpan">4</span> and <span  class="mySpan">15</span> characters in length</p>
                <p>· eg: zsyzsy</p>
              </div>
              <el-input
                type="text"
                slot="reference"
                v-model="registerForm.username"
                auto-complete="off"
                placeholder="Username"
                prefix-icon="el-icon-user"
              ></el-input>
            </el-popover>
          </el-form-item>

          <el-form-item prop="email" >
            <el-popover
              placement="right"
              width="200"
              trigger="focus"
            >
              <div style="font-size:small">
                <p>· eg: zsyzsy@qq.com</p>
              </div>
              <el-input
                type="text"
                slot="reference"
                v-model="registerForm.email"
                auto-complete="off"
                placeholder="Email"
                prefix-icon="el-icon-message"
              ></el-input>
            </el-popover>
          </el-form-item>

          <el-form-item prop="password">
            <el-popover
              placement="right"
              width="200"
              trigger="focus"
            >
              <div style="font-size:small" >
                <p>· <span class="mySpan">6</span> and <span class="mySpan">12</span> characters in length</p>
                <p>· eg: pAs3s?wOrd</p>
                <p>· password strength</p>
                <p>
                  <span>
                    <el-progress 
                    v-if="strength===0" 
                    :percentage=0 
                    :format="format" 
                    :color="customColors">
                    </el-progress>

                    <el-progress 
                    v-if="strength===1" 
                    :percentage=25
                    :format="format" 
                    :color="customColors">
                    </el-progress>

                    <el-progress 
                    v-if="strength===2" 
                    :percentage=50
                    :format="format" 
                    :color="customColors">
                    </el-progress>

                    <el-progress 
                    v-if="strength===3" 
                    :percentage=75
                    :format="format" 
                    :color="customColors">
                    </el-progress>

                    <el-progress 
                    v-if="strength===4" 
                    :percentage=100 
                    :format="format"
                    :color="customColors">
                    </el-progress>
                  </span>
                </p>
              </div>
              <el-input
                type="password"
                slot="reference"
                show-password
                v-model="registerForm.password"
                auto-complete="off"
                prefix-icon="el-icon-lock"
                placeholder="Password"
                :@change="calculateStrength"
              ></el-input>
            </el-popover>
          </el-form-item>

          <el-form-item prop="confirmPassword" >
            <el-popover
              placement="right"
              width="200"
              trigger="focus"
            >
              <div style="font-size:small">
                <p>· Equal to password</p>
                <p>· eg: pAs3s?wOrd</p>
              </div>
              <el-input
              type="password"
              slot="reference"
              show-password
              v-model="registerForm.confirmPassword"
              auto-complete="off"
              prefix-icon="el-icon-lock"
              placeholder="Confirm password"
            ></el-input>
            </el-popover>
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
                v-model="registerForm.verifycode" 
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
              v-on:click="register">
                Register
            </button>
            <button v-else
              type="button"
              class="layui-btn"
              native-type="submit"
              size="medium"
              style="width:100% "
              v-on:click="register">
                Register
            </button>
          </el-form-item>

          <el-form-item size="medium">
            <button 
              type="button"
              style="width:100%"
              @click="resetForm('registerForm')"
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
    name: "Register",
    components: {SIdentify},
    data() {
      return {
        strength: 1, 
				identifyCodes: '1234567890',
				identifyCode: '',
        customColors: [
          {color: '#909399', percentage: 26},
          {color: '#f56c6c', percentage: 51},
          {color: '#E6A23C', percentage: 76},
          {color: '#67C23A', percentage: 100}
        ],
        registerForm: {
          username: "",
          email:"",
          password: "",
          confirmPassword: "",
          verifycode: '',
        },
        rules: {
          username: [
            {required: true,message:"Username is required",blur:"change"},
            {pattern: /^[\w]{4,15}$/,message:"Invalid username",blur:"change"}
          ],
          email: [
            {required: true,message:"Email is required",blur:"change"},
            {pattern: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, message:"Invalid email", trigger:"change"},
          ],
          password: [
            {required:true, message:"Password is required", blur:"change"},
            {pattern:/^[\w\W]{6,12}$/, message:"Invalid password",blur:"change"},
            {
              validator:(rule,value,callback)=>{
                if(value !== this.registerForm.confirmPassword) {
                  callback(new Error('Password doesn\'t equals confirm password'));
                }
                callback();
              },
              message:"Password doesn\'t equals confirm password",
              trigger:"change"
            },
          ],
          confirmPassword: [
            {required:true, message:"Confirm password is required", blur:"change"},
            {
              validator:(rule,value,callback)=>{
                if(value !== this.registerForm.password) {
                  callback(new Error('Confirm password doesn\'t equals password'));
                }
                callback();
              },
              message:"Confirm password doesn\'t equals password",
              trigger:"change"
            },
            {pattern:/^[\w\W]{6,12}$/, message:"Invalid confirm password",blur:"change"},
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
          return !(/^[\w]{4,15}$/.test(this.registerForm.username)
                  &&(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(this.registerForm.email))
                  &&(/^[\w\W]{6,12}$/.test(this.registerForm.password))
                  &&(/^[\w\W]{6,12}$/.test(this.registerForm.confirmPassword))
                  && this.registerForm.confirmPassword === this.registerForm.password
                  && this.registerForm.verifycode === this.identifyCode
                  );
      },
      calculateStrength(){
        var modes = 0;
        if(/\d/.test(this.registerForm.password)) modes++; //数字
        if(/[a-z]/.test(this.registerForm.password)) modes++; //小写
        if(/[A-Z]/.test(this.registerForm.password)) modes++; //大写  
        if(/\W/.test(this.registerForm.password)) modes++; //特殊字符
        //逻辑处理
        switch(modes) {
          case 0:
            this.strength = 0;
            break;
          case 1:
            this.strength = 1;
            break;
          case 2:
            this.strength = this.registerForm.password.length < 8 ? 1 : 2;
            break;
          case 3:
            this.strength = this.registerForm.password.length < 10 ? 2 : 3;
            break;
          case 4:
            this.strength = this.registerForm.password.length < 10 ? 3 : 4;
            break;
        }
      },
    },
    methods: {
      register() {
        // Turn to loading mode when the form is submitted,and come back when getting response
        this.loading = true;
        this.$axios
          .post("/register", {
            username: this.registerForm.username,
            password: this.registerForm.password,
            email: this.registerForm.email
          })
          .then(resp => {
            if (resp.status === 200 && resp.data.hasOwnProperty("token")) {
              // login automatically after register
              this.$store.commit("login", resp.data);
              this.$notify({
                dangerouslyUseHTMLString: true,
                type:'success',
                title: 'Register success',
                message: '<strong style="color:teal"Register successfully</strong>',
              });
              this.$router.replace({ path: "/" });              
            } else {
              this.errorNotification(resp.data);
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
      format(percentage) {
        if(percentage <= 25)
          return 'bad';
        if(percentage <= 50)
          return 'low';
        if(percentage <= 75)
          return 'mid';
        if(percentage <= 100)
          return 'high';
      },
      errorNotification(message){
        if(message === "username"){
          this.$notify({
            type:'error',
            dangerouslyUseHTMLString: true,
            title: 'Register error',
            message: '<strong style="color:teal">The username has been used. Please try another one</strong>'
          });
        }
        else{
          this.$notify({
            type:'error',
            dangerouslyUseHTMLString: true,
            title: 'Register error',
            message: '<strong style="color:teal">The email has been used. Please try another one</strong>'
          });
        }
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