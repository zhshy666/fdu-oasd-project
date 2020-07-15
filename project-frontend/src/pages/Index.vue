<template>
    <el-container>
      <el-header>
        <div v-if="beforeLogin">
          <el-button 
            type="primary"
            @click="login">
              Login
          </el-button>
          <el-button 
            type="primary"
            @click="register">
              Register
          </el-button>

        </div>
        <div v-if="afterLogin">
          Hello, {{msg}}
          <br>
          <router-link to @click.native="logout">
              Log out
          </router-link>
        </div>
      </el-header>
    </el-container>
</template>

<script>
export default {
  inject: ['reload'],
  name: "Index",
  data() {
    return {
      beforeLogin: true,
      afterLogin: false,
      msg: this.$store.state.cur_user
    };
  },
  methods: {
    login(){
      this.$router.replace({ path: "/login" });
    },
    register(){
      this.$router.replace({path: "/register"})
    },
    logout() {
      this.$confirm("Are you sure to log out?", "Logout", {
        confirmButtonText: "Yes",
        cancelButtonText: "No"
      })
      .then(() => {
        this.$store.commit("logout");
        this.reload();
        this.$notify({
          type:'info',
          dangerouslyUseHTMLString: true,
          message: '<strong style="color:teal">Logout successfully</strong>',
        });
      })
      .catch(error => {});
    }
  },
  created() {
    // Control the display of different interface
    if (this.$store.state.token) {
      this.beforeLogin = false;
      this.afterLogin = true;
    }
  }
};
</script>