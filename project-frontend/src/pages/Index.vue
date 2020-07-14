<template>
    <el-container>
        <div v-if="beforeLogin">
          <el-button plain @click="login">Login</el-button>
        </div>
        <div v-if="afterLogin">
          Hello, {{msg}}
          <br>
          <router-link to @click.native="logout">
              Log out
          </router-link>
        </div>
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
    logout() {
      this.$confirm("Are you sure to log out?", "Log Out", {
        confirmButtonText: "Yes",
        cancelButtonText: "No"
      })
      .then(() => {
        this.$store.commit("logout");
        this.reload();
        this.$message({
          dangerouslyUseHTMLString: true,
          message: '<strong style="color:teal">You have signed out!</strong>',
          center: true
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