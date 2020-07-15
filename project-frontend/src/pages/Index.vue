<template>
<div>
<navbar></navbar>
<el-container>

</el-container>
</div>
</template>

<script>
import navbar from "../components/Navbar"
export default {
  components: {navbar},
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