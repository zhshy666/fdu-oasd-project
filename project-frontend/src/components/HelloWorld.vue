<!--修改模板如下-->
<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <h2>Essential Links</h2>
        <el-button type="primary" @click="visible = true">Button</el-button>
            <el-dialog :visible.sync="visible" title="Hello world">
            <p>Try Element</p>
        </el-dialog>
        <br><br>
        <el-button type="primary" @click="getRequest">Get测试</el-button>
        <el-button type="primary" @click="postRequest">Post测试</el-button>
    <h2>Ecosystem</h2>
  </div>
</template>


<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      visible: false,
      msg: 'Welcome to Your Vue.js App'
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
