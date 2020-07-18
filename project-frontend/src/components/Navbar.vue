<template>
<el-container>
  <el-main>
    <el-col :span="20" :offset="2">
      <el-menu 
        mode="horizontal"
        :router="true"
        @select="handleSelect">
        <el-menu-item index="/" class="myLogo">
            Travellers
        </el-menu-item>
        <el-menu-item index="/search">
        <i class="el-icon-search"></i>
        </el-menu-item>
        <el-menu-item 
          v-if="beforeLogin"
          class="myMenu"
          index="/login">
          <i class="el-icon-s-promotion"></i>
            Login
        </el-menu-item>

        <el-submenu
          v-else
          class="myMenu"
          index="/"
        >
        <template slot="title"><i class="el-icon-user"></i>{{username}}</template>
            <el-menu-item index="/" class="myItem">
              Home
            </el-menu-item>           
            <el-menu-item index="/" class="myItem">Upload</el-menu-item>
            <el-menu-item index="/" class="myItem">Friends</el-menu-item>
            <el-menu-item index="/" @click="logout" class="myItem"> Logout</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-col>
  </el-main>
  
</el-container>
</template>

<script>
  export default {
      name: "navbar",
      inject: ['reload'],
    data() {
      return {
        input: '',
        beforeLogin: true,
        username: this.$store.state.cur_user
      };
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
      logout() {
      this.$confirm("Are you sure to log out?", "Log out", {
        confirmButtonText: "Yes",
        cancelButtonText: "No"
      })
      .then(() => {
        this.$store.commit("logout");
        this.reload();
        this.$notify({
          type:'info',
          dangerouslyUseHTMLString: true,
          title: 'Log out success',
          message: '<strong style="color:teal">Log out successfully</strong>',
        });
      })
      .catch(error => {});
    }
    },
    created() {
        // Control the display of different interface
        if (this.$store.state.token) {
            this.beforeLogin = false;
        }
    }
  }
</script>

<style scoped>
.myLogo{
    color: #009688;
    font-size: larger;
    margin-left: 20px;
}
.myMenu{
  margin-left: 70%;
}
.myItem{
  text-align: center;
}
</style>