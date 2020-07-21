<template>
<div>
    <navbar></navbar>
    <el-container>
      <el-main>
          <el-col :span="6" :offset="1">
            <span class="mySpan">Messages</span>
          </el-col>
        <el-col :span="18" :offset="3">
            <el-divider></el-divider>
            <div v-if="noMessage">No messages.</div>
            <div v-if="hasMessage" class=" layui-row" >
                <el-card class="box-card myCard" v-for="(message,i) in messages.slice((currentPage-1)*6,currentPage*6)" :key="i">
                    <div 
                    slot="header" 
                    class="clearfix"
                    >
                        <el-badge :is-dot="isNew(message)"></el-badge>&nbsp;&nbsp;&nbsp;
                        <span>{{message.title}}</span>&nbsp;&nbsp;&nbsp;
                    </div>
                    <div class="text">
                        <i class="content">{{message.content}}</i>
                        <br>
                        <br>
                        <br>
                        <i class="date">{{message.sentTime}}</i>
                    </div>
                </el-card>
            </div>
            <div v-if="hasMessage" class="block">
                <br>
                <br>
                <el-pagination
                    layout="prev, pager, next"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-size="6"
                    :total="total">
                </el-pagination>
            </div>
        </el-col>
      </el-main>
    </el-container>
</div>
</template>

<script>
import navbar from "../components/Navbar"

export default {
    components: {navbar},
    name: 'message',
    data(){
        return{
            messages: [],
            hasMessage: false,
            noMessage: true,
            total: 0,
            currentPage: 1,
        }
    },
    created(){
        if(!this.$store.state.token){
            this.$router.replace("/login");
            this.$notify({
                type:'error',
                dangerouslyUseHTMLString: true,
                title: 'Request error',
                message: '<strong style="color:teal">Please login to view your messages.</strong>'
            });
            this.$router.replace("/login");
        }
        this.$axios
            .get("/getMessages",{})
            .then(resp => {
                if(resp.status === 200){
                    this.messages = resp.data;
                    if(this.messages.length > 0){
                        this.hasMessage = true;
                        this.noMessage = false;
                    }
                }
                else {
                    this.errorNotification();
                }
            })
            .catch(error => {
                console.log(error);
                this.errorNotification();
            });
    },
    methods: {
        handleCurrentChange(val){
            this.currentPage = val;
        },
        isNew(message){
            return message.status === -1;
        },
        errorNotification(){
        this.$notify({
          type:'error',
          dangerouslyUseHTMLString: true,
          title: 'Request error',
          message: '<strong style="color:teal">Requset error, please try again.</strong>'
        });
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
.myCard{
    width: 400px;
    height: 200px;
    margin: 50px 20px 20px 50px;
}
.content{
    /* font-size: medium; */
    font-style: normal;
}
.date{
    font-size: small;
    font-style: normal;
    color: gray;
}
</style>