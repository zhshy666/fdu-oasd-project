<template>
<div>
<navbar></navbar>
<carousel></carousel>
<el-container>
  <el-main>
    
  </el-main>
</el-container>
</div>
</template>

<script>
import navbar from "../components/Navbar"
import carousel from "../components/Carousel"
export default {
  components: {navbar, carousel},
  name: "Index",
  data() {
    return {
      beforeLogin: true,
      afterLogin: false,
      msg: this.$store.state.cur_user
    };
  },
  methods: {
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