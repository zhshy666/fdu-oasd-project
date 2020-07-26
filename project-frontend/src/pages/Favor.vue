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
                <el-tab-pane label="Favors" name="first"><favors></favors></el-tab-pane>
            </el-tabs>
          </el-col>
        </el-main>
    </el-container>
</div>
</template>

<script>
import navbar from "../components/Navbar"
import favors from "../components/Favors"

export default {
    name: 'favor',
    components: {navbar, favors},
    data() {
        return{
            username: '',
            activeName: 'first',
        }
    },
    created() {
        console.log(this.$route.params);
        if(this.$route.params.username){
            this.username = this.$route.params.username;
        }
        if(!this.$store.state.token){
            this.$router.replace("/login");
            this.$message.error('Please login to visit favors of your friends');
        }
        if(this.username !== this.$store.state.cur_user){
            this.$axios
                .post("/isFriend",{
                    username: this.username
                })
                .then(resp => {
                    if(resp.status === 200){
                        if(resp.data === 'false'){
                            this.$router.replace("/home");
                            this.$message.error('You are not friends yet');
                        }
                    }
                })
                .catch(error =>{
                    console.log(error);
                });
        }
    },
    methods: {
        handleClick(tab, event) {
            console.log(tab, event);
        },
    }
}
</script>