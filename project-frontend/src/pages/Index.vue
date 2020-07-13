<template>
    <el-container>
        <div v-if="beforeLogin">
          <a href="Login">login</a>
        </div>
        <div v-if="afterLogin">
          Hello{{msg}}
        </div>
    </el-container>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      beforeLogin: true,
      afterLogin: false,
      msg: '111'
    };
  },
  methods: {
    
  },
  created() {
    this.$axios
    .get()
    // Control the display of different interface
    if (this.$store.state.token) {
      this.beforeLogin = false;
      this.afterLogin = true;
    }
  }
};
</script>