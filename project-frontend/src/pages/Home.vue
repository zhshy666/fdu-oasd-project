<template>
<div>
    <navbar></navbar>
    <el-container>
        <el-main>
          <el-col :span="7" :offset="0">
            <span class="mySpan">{{username}} 's Home</span>
            <br>
            <br>
            <br>
          </el-col>
          
          <el-col :span="18" :offset="3">
              <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="Uploads" name="first"><uploads></uploads></el-tab-pane>
                <el-tab-pane label="Favors" name="second">Favors</el-tab-pane>
                <el-tab-pane label="Friends" name="third">Friends</el-tab-pane>
                <el-tab-pane label="Messages" name="fourth">Messages</el-tab-pane>
            </el-tabs>
          </el-col>
        </el-main>
    </el-container>
</div>
</template>

<script>
import navbar from "../components/Navbar"
import footerbar from "../components/footer"
import uploads from "../components/Uploads"

export default {
    name: "Home",
    components: {navbar, footerbar, uploads},
    data(){
        return{
            username: '',
            activeName: 'first'
        }
    },
    created(){
        // get login info
        if(!this.$store.state.token){
            this.$router.replace("/login");
            this.$notify({
                type:'error',
                dangerouslyUseHTMLString: true,
                title: 'Request error',
                message: '<strong style="color:teal">Please login to visit your home page.</strong>'
            });
        }else{
            this.username = this.$store.state.cur_user;
        }
    },
    methods: {
        handleClick(tab, event) {
            console.log(tab, event);
        },
    }
}
</script>

<style scoped>
.mySpan{
  color: #009688;
  font-size: larger;
  margin-left: 20px;
}
</style>