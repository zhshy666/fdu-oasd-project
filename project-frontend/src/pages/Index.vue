<template>
<div>
<navbar></navbar>
<carousel></carousel>
<newestImages></newestImages>
<el-container>
</el-container>
</div>
</template>

<script>
import navbar from "../components/Navbar"
import carousel from "../components/Carousel"
import newestImages from "../components/NewestImages"
export default {
  components: {navbar, carousel, newestImages},
  name: "index",
  data() {
    return {
      beforeLogin: true,
      afterLogin: false,
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