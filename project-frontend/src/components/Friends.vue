<template>
    <el-container>
        <el-main>
            <br>
            <el-col :span="3" :offset="0">
                <el-button 
                @click="formVisible = true"
                size="medium" 
                plain
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
                        ref="userTable"
                        :data="users.slice((currentUserPage-1)*5,currentUserPage*5)"
                        tooltip-effect="dark"
                        style="width: 100%"
                        @selection-change="handleSelectionChange">
                        <el-table-column
                        type="selection"
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
                        width="200">
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
            </el-col>

            <el-col :span="13" v-if="hasFriend" :offset="2">
                <el-table
                    :data="friends.slice((currentPage-1)*10,currentPage*10)"
                    stripe
                    :key="i"
                    style="width: 100%">
                    <el-table-column
                    label="Username"
                    width="180">
                    <template slot-scope="scope">
                        <router-link :to="'/'+scope.row.username">{{ scope.row.username }}</router-link>
                    </template>
                    </el-table-column>

                    <el-table-column
                    label="Email"
                    width="200">
                    <template slot-scope="scope">
                        <router-link :to="'/'+scope.row.email">{{ scope.row.email }}</router-link>
                    </template>
                    </el-table-column>

                    <el-table-column
                    prop="dateJoined"
                    label="Date joined"
                    width="170">
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
            formLabelWidth: '120px',
            beforeSearch: true,
            afterSearch: false,
            hasFriend: false,
            noFriend: true,
            users: [],
            sendUsers: [],
            selections:[],
            friends: [],
            total: '',
            totalUser: '',
            currentPage: 1,
            currentUserPage: 1,
            form: {
                input:''
            },
        }
    },
    created(){
        this.$axios
            .get("/getFriends", {})
            .then(resp => {
                if(resp.status === 200){
                    this.friends = resp.data;
                    if(this.friends.length > 0){
                        this.hasFriend = true;
                        this.noFriend = false;
                        this.total = this.friends.length;
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
            console.log(this.selections.length);
            console.log(this.afterSearch);
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
</style>