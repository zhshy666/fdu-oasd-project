<template>
    <el-container>
        <el-main>
            <el-col :span="5" :offset="19">
                <el-button 
                @click="formVisible = true"
                size="medium" 
                type="primary" 
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
                        height="300px"
                        v-if="afterSearch"
                        ref="userTable"
                        :data="users"
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
                </el-dialog>
                &nbsp;&nbsp;&nbsp;
                <el-button 
                @click="form2Visible = true"
                size="medium" 
                type="primary" 
                icon="el-icon-info">
                    Status
                </el-button>
                <el-dialog center title="Message status" :visible.sync="form2Visible">
                    <el-table
                        height="300px"
                        ref="userTable"
                        :data="users"
                        tooltip-effect="dark"
                        style="width: 100%"
                        @selection-change="handleSelectionChange">
                        
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
                        label="Status"
                        show-overflow-tooltip>
                        </el-table-column>
                    </el-table>
                </el-dialog>
            </el-col>
            <el-col :span="24">
            </el-col>
        </el-main>
    </el-container>
</template>

<script>
export default {
    name: "friends",
    data(){
        return{
            formVisible: false,
            form2Visible: false,
            formLabelWidth: '120px',
            beforeSearch: true,
            afterSearch: false,
            users: [],
            selections:[],
            form: {
                input:''
            },
        }
    },
    created(){

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
            this.$axios
                .post("/sendRequest",{
                    users: this.selections
                })
                .then(resp => {
                    if(resp.status === 200){
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