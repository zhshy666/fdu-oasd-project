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
                <div class=" layui-col-md4 card layui-col-md-offset2" v-for="(message,i) in messages.slice((currentPage-1)*6,currentPage*6)" :key="i">
                <el-card class="box-card myCard">
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
                        <br>
                        <br>
                        <br>
                        <div v-if="isRequest(message) && isNew(message)">
                            <el-button type="primary" size="medium" v-on:click="accept(message)">Accept</el-button>&nbsp;&nbsp;&nbsp;
                            <el-button plain size="medium" v-on:click="reject(message)">Reject</el-button>
                        </div>
                        <div v-if="isRequest(message) && acceptRequest(message)">
                            <el-button type="primary" size="medium" disabled>Accept</el-button>&nbsp;&nbsp;&nbsp;
                        </div>
                        <div v-if="isRequest(message) && rejectRequest(message)">
                            <el-button plain size="medium" disabled>Reject</el-button>
                        </div>
                        
                        <div v-if="isResponse(message) && isNew(message)">
                            <el-button plain size="medium" v-on:click="hasRead(message)">Mark as read</el-button>
                        </div>
                        <div v-if="isResponse(message) && !isNew(message)">
                            <el-button plain size="medium" disabled>Mark as read</el-button>
                        </div>
                    </div>
                </el-card>
                </div>
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
    inject: ['reload'],
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
            this.$message.error('Please login to view your messages');
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
        accept(message){
            this.$axios
                .post("/acceptMessage",{
                    messageId: message.messageId,
                    to: message.fromId,
                })
                .then(resp => {
                    if(resp.status === 200){
                        this.reload();
                        this.$message({
                            type: "success",
                            message: 'Accept friend request successfully',
                        });
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
        reject(message){
            this.$axios
                .post("/rejectMessage",{
                    messageId: message.messageId,
                    to: message.fromId
                })
                .then(resp => {
                    if(resp.status === 200){
                        this.reload();
                        this.$message('Reject friend request successfully');
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
        isNew(message){
            return message.status == -1;
        },
        isRequest(message){
            return message.title == 'Friend Request';
        },
        isResponse(message){
            return message.title == 'Friend Response';
        },
        acceptRequest(message){
            return message.status === 1;
        },
        rejectRequest(message){
            return message.status === 2;
        },
        hasRead(message){
            this.$axios
                .post("/hasReadMessage",{
                    messageId: message.messageId,
                })
                .then(resp => {
                    if(resp.status === 200){
                        this.reload();
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
        errorNotification(){
            this.$message.error('Requset error, please try again');
        },
    },
    computed: {

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
    height: 240px;
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
.card{
    margin: -10px 20px 20px 60px;
}
</style>