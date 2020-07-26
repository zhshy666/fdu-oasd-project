<template>
    <el-container>
        <el-main>
            <br>
            <el-col :span="3" :offset="0">
                <el-button 
                @click="formVisible = true"
                size="medium" 
                type="primary"
                style="width: 80%"
                icon="el-icon-plus">
                    Add
                </el-button>
                <el-dialog center title="Add friends" :visible.sync="formVisible">
                    <el-form :model="form" :inline="true">
                        <el-form-item prop="input">
                            <el-input
                                type="text"
                                v-model="form.input"
                                auto-complete="off"
                                placeholder="Search for users"
                                prefix-icon="el-icon-search"
                                style="width: 400px; margin-left: 30px"
                            ></el-input>
                        </el-form-item>

                        <el-form-item size="medium">
                            <el-button
                            type="primary"
                            native-type="submit"
                            size="medium"
                            style="width:100%"
                            v-on:click="search"
                            :disabled="disableSearch">
                                Search
                            </el-button>
                        </el-form-item>

                        <el-form-item size="medium">
                            <el-button
                            type="primary"
                            size="medium"
                            style="width:100%"
                            v-on:click="send"
                            :disabled="disableAdd">
                                Send request
                            </el-button>
                        </el-form-item>
                    </el-form>
                    <el-table
                        v-if="afterSearch"
                        height="300px"
                        ref="userTable"
                        :data="users.slice((currentUserPage-1)*5,currentUserPage*5)"
                        tooltip-effect="dark"
                        :row-key="getRowKeys"
                        style="width: 100%"
                        @selection-change="handleSelectionChange">
                        <el-table-column
                        type="selection"
                        :reserve-selection="true"
                        width="55">
                        </el-table-column>
                        <el-table-column
                        prop="username"
                        label="Username"
                        width="150">
                        </el-table-column>
                        <el-table-column
                        prop="email"
                        label="Email"
                        width="210">
                        </el-table-column>
                        <el-table-column
                        prop="dateJoined"
                        label="Date joined"
                        show-overflow-tooltip>
                        </el-table-column>
                    </el-table>
                    <div v-if="afterSearch" class="block" style="text-align: center">
                        <el-pagination
                            layout="prev, pager, next"
                            @current-change="handleCurrentUserChange"
                            :current-page="currentUserPage"
                            :page-size="5"
                            :total="totalUser">
                        </el-pagination>
                    </div>
                </el-dialog>
                <br>
                <br>
                <el-button 
                @click="form2Visible = true"
                size="medium" 
                type="primary"
                style="width: 80%"
                icon="el-icon-info">
                    Status
                </el-button>
                <el-dialog width="600px" center title="Check status" :visible.sync="form2Visible">
                    <el-table
                        height="300px"
                        ref="messageTable"
                        :data="messages.slice((currentMessagePage-1)*5,currentMessagePage*5)"
                        tooltip-effect="dark"
                        :row-key="getRowKeys"
                        style="width: 100%"
                    >
                        <el-table-column
                        prop="username"
                        label="Username"
                        width="180">
                        </el-table-column>
                        <el-table-column
                        prop="sentTime"
                        label="Sent time"
                        width="200">
                        </el-table-column>
                        <el-table-column
                        label="Status">
                        <template slot-scope="scope">
                            <el-tag type="info" v-if="scope.row.status === -1">Unread</el-tag>
                            <el-tag type="danger" v-if="scope.row.status === 2">Reject</el-tag>
                            <el-tag type="success" v-if="scope.row.status === 1">Accept</el-tag>
                        </template>
                        </el-table-column>
                    </el-table>
                    <div v-if="hasMessage" class="block" style="text-align: center">
                        <el-pagination
                            layout="prev, pager, next"
                            @current-change="handleCurrentMessageChange"
                            :current-page="currentMessagePage"
                            :page-size="5"
                            :total="totalMessage">
                        </el-pagination>
                    </div>
                </el-dialog>
            </el-col>

            <el-col :span="14" v-if="hasFriend" :offset="2">
                <el-table
                    :data="friends.slice((currentPage-1)*10,currentPage*10)"
                    stripe
                    style="width: 100%"
                >
                    <el-table-column
                      label="Username"
                      width="180">
                        <template slot-scope="scope">
                            <router-link :to="'favor/'+scope.row.username">{{ scope.row.username }}</router-link>
                        </template>
                    </el-table-column>

                    <el-table-column
                      label="Email"
                      width="200">
                        <template slot-scope="scope">
                            <router-link :to="'favor/'+scope.row.username">{{ scope.row.email }}</router-link>
                        </template>
                    </el-table-column>

                    <el-table-column
                      prop="dateJoined"
                      label="Date joined"
                      width="170">
                    </el-table-column>

                    <el-table-column>
                        <template slot-scope="scope">
                            <el-link @click="chat(scope)" :underline="false"><i class="el-icon-chat-dot-round"></i></el-link>
                            <el-dialog 
                              @close='closeDialog' 
                              center title="Chatting now" 
                              :visible.sync="form3Visible"
                              style="width: 60%; margin-left: 20%"
                            >
                                <div style="height:240px;overflow-y:auto; margin-bottom:10%">
                                    <div v-for="(value,key,index) in messageList" :key="index">
                                        <i v-if="value.name==name" class="chatNameR">me</i>
                                        <br v-if="value.name==name">
                                        <el-tag v-if="value.name==name" type="success" style="float:right; margin-right: 10%;">{{value.msg}}</el-tag>
                                        <br/>
                                        
                                        <i v-if="value.name!=name" class="chatNameL">{{value.name}}</i>
                                        <br v-if="value.name!=name">
                                        <el-tag v-if="value.name!=name" style="float:left; margin-left: 5%">{{value.msg}}</el-tag>
                                        <br/>
                                    </div>
                                </div>
                                <el-row>
                                    <el-form 
                                        @submit.native.prevent
                                        status-icon
                                        :model="chatForm"
                                        ref="chatForm"
                                        label-position="left"
                                        label-width="0px"
                                    >
                                    <el-form-item prop="messageValue">
                                        <el-input
                                            type="textarea"
                                            :autosize="{ minRows: 3, maxRows: 4}"
                                            placeholder="Say something here..."
                                            prefix-icon="el-icon-s-promotion"
                                            maxlength="15"
                                            show-word-limit
                                            v-model="chatForm.messageValue">
                                        </el-input>
                                    </el-form-item>

                                    <el-form-item size="medium">
                                        <el-button
                                        type="primary"
                                        native-type="submit"
                                        size="medium"
                                        class="btn"
                                        @click="sendMessage()">
                                            Send
                                        </el-button>
                                        <el-button 
                                        style="medium"
                                        plain
                                        @click="resetForm('chatForm')"
                                        >
                                            Reset
                                        </el-button>
                                    </el-form-item>
                                    </el-form>
                                </el-row>
                            </el-dialog>
                        </template>
                    </el-table-column>
                </el-table>

                <div v-if="hasFriend" class="block">
                    <br>
                    <br>
                    <el-pagination
                        layout="prev, pager, next"
                        @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-size="10"
                        :total="total">
                    </el-pagination>
                </div>
            </el-col>
            <el-col :span="24" v-if="noFriend">
                <br>
                <br>
                <br>
                No friends now. Click <el-link :underline="false" @click="formVisible = true">here</el-link> to add more friends.
            </el-col>
        </el-main>
    </el-container>
</template>

<script>
export default {
    name: "friends",
    inject: ['reload'],
    data(){
        return{
            formVisible: false,
            form2Visible: false,
            form3Visible:false,
            beforeSearch: true,
            afterSearch: false,
            hasFriend: false,
            noFriend: true,
            users: [],
            sendUsers: [],
            selections:[],
            friends: [],
            messages: [],
            hasMessage: false,
            total: '',
            totalUser: '',
            totalMessage: '',
            currentPage: 1,
            currentUserPage: 1,
            currentMessagePage: 1,
            form: {
                input:''
            },
            chatForm: {
                messageValue: "", // 消息内容
            },
            websocket: null, // WebSocket对象
            aisle: "", // 对方频道号
            messageList: [], // 消息列表
            name: ''
        }
    },
    created(){
        this.$axios
            .get("/getFriends", {})
            .then(resp => {
                if(resp.status === 200){
                    this.friends = resp.data.friends;
                    this.messages = resp.data.messages;
                    this.total = this.friends.length;
                    this.totalMessage = this.messages.length;
                    this.name = this.$store.state.cur_user;
                    if(this.friends.length > 0){
                        this.hasFriend = true;
                        this.noFriend = false;
                    }
                    if(this.totalMessage > 0){
                        this.hasMessage = true;
                    }
                }
                else {
                    this.errorNotification();
                }
            })
            .catch(error =>{
                console.log(error);
            })
    },
    methods: {
        search(){
            this.$axios
                .post("/findUserByUsername",{
                    username: this.form.input
                })
                .then(resp => {
                    if(resp.status === 200){
                        this.users = resp.data;
                        this.afterSearch = true;
                        this.totalUser = this.users.length;
                    }
                    else {
                        this.errorNotification();
                    }
                })
                .catch(error =>{
                    console.log(error);
                })
        },
        handleSelectionChange(val) {
            this.selections = val;
        },
        send(){
            var len = this.selections.length;
            for (let i = 0; i < len; i++) {
                this.sendUsers.push(this.selections[i].id);
            }
            console.log(this.sendUsers);
            this.$axios
                .post("/sendRequest",{
                    sendUsers: this.sendUsers
                })
                .then(resp => {
                    if(resp.status === 200){
                        this.reload();
                        this.$notify({
                            type:'success',
                            dangerouslyUseHTMLString: true,
                            title: 'Send request success',
                            message: 
                            '<strong style="color:teal">Send request successfully!</strong>'
                        });
                    }
                    else {
                        this.errorNotification();
                    }
                })
                .catch(error =>{
                    console.log(error);
                })
        },
        handleCurrentChange(val){
            this.currentPage = val;
        },
        handleCurrentUserChange(val){
            this.currentUserPage = val;
        },
        handleCurrentMessageChange(val){
            this.currentMessagePage = val;
        },
        getRowKeys(row){
            return row.id;
        },
        title(scope){
            return scope.row.username;
        },
        chat(scope){
            this.form3Visible = true;
            this.connectWebSocket();
            this.aisle = scope.row.username;
        },
        connectWebSocket: function() {
            console.log("建立连接");
            // support or not
            if ("WebSocket" in window) {
                this.websocket = new WebSocket(
                    "ws://localhost:8082/websocket/" + this.$store.state.cur_user
                );
            } else {
                alert("not support");
            }

            this.websocket.onerror = function() {
                console.log("error");
            };
            
            //success
            this.websocket.onopen = function(event) {
                console.log("connect success");
            };

            //message
            var that = this;
            this.websocket.onmessage = function(event) {
                console.log(event.data);
                var object = eval("(" + event.data + ")");
                if(object.false){
                    console.log("not online");
                    that.$notify({
                        type:'error',
                        dangerouslyUseHTMLString: true,
                        title: 'Not online',
                        message: '<strong style="color:teal">Sorry, the user is not online now. Please send messages later.</strong>'
                    });
                }
                else{
                    if (object.type == 0) {
                        console.log("success");
                        // that.showInfo(object.people, object.aisle);
                    }
                    if (object.type == 1) {
                        console.log("message");
                        that.messageList.push(object);
                    }
                }
            };

            this.websocket.onclose = function() {
                console.log("Loc MSG:关闭连接");
            };

            window.onbeforeunload = function() {
                this.websocket.close();
            };
        
        },
        sendMessage: function() {
            var socketMsg = { msg: this.chatForm.messageValue, toUser: this.aisle };
            if (this.aisle == "") {
                //群聊
                socketMsg.type = 0;
            } else {
                //单聊
                socketMsg.type = 1;
            }
            this.websocket.send(JSON.stringify(socketMsg));
            this.chatForm.messageValue = "";
        },
        showInfo: function(people, aisle) {
            this.$notify({
                title: "当前在线人数：" + people,
                message: "您的频道号：" + aisle,
                duration: 0
            });
        },
        closeDialog(){
            this.websocket.close();
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        errorNotification(){
            this.$notify({
                type:'error',
                dangerouslyUseHTMLString: true,
                title: 'Request error',
                message: '<strong style="color:teal">Requset error, please try again.</strong>'
            });
        },
    },
    computed: {
        disableSearch(){
            return !(this.form.input);
        },
        disableAdd(){
            return !(this.afterSearch && this.selections.length > 0);
        }
    }
}
</script>

<style scoped>
.icon{
    font-size: 30px;
    color: gray;
}
.text{
    font-size: small;
    line-height: 30px;
    color: black;
}
.noResults{
    text-align: center;
}
.chatNameR{
    float:right; 
    font-size:small; 
    font-style:normal; 
    margin-right: 10%
}
.chatNameL{
    float:left; 
    font-size:small; 
    font-style:normal; 
    margin-left: 5%
}
</style>