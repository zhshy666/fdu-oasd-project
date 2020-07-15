<template>
<el-container>
  <el-main>
    <el-col :span="20" :offset="2">
      <el-menu 
        :default-active="activeIndex" 
        mode="horizontal"
        :router="true"
        @select="handleSelect">
        <el-menu-item index="/" class="myLogo">
            Travellers
        </el-menu-item>
        <el-menu-item>
            <el-input 
                v-model="input"
                size="small"
                prefix-icon="el-icon-search"
                placeholder="Serach for something...">
            </el-input>
        </el-menu-item>
        <el-menu-item 
          v-if="beforeLogin"
          index="/login">
            Login
        </el-menu-item>
        <el-menu-item 
          v-if="beforeLogin" 
          index="/register">
            Register
        </el-menu-item>

        <el-submenu
          v-else
        >
        <template slot="title">{{username}}</template>
            <el-menu-item index="/">Home</el-menu-item>
            <el-menu-item index="/">Upload</el-menu-item>
            <el-menu-item index="/">Friends</el-menu-item>
            <el-menu-item index="/" @click="logout"> Logout</el-menu-item>
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
        activeIndex: '1',
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
}
.myImg{
    width: 40%;
    height: 40%;
    margin-top: -3px;
}

</style>