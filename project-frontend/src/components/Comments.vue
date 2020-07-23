<template>
<el-container>
    <el-main>
        <el-col :span="16" :offset="4">
            <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="Sort by heat" name="first">
                <br>
                <div v-if="!hasComment">No comments. Write down your comments now!</div>
                <div v-if="hasComment" class=" layui-row">
                    <div class=" layui-col-md7" v-for="(comment,i) in comments.slice((currentPage-1)*10,currentPage*10)" :key="i">
                        <el-card class="box-card" shadow="hover">
                            <div slot="header" style="text-align: left">
                                <span style="text-align: left" class="el-icon-user">&nbsp;&nbsp;{{comment.username}}</span>
                            </div>
                            <div style="text-align: left">
                                {{comment.content}}
                            </div>
                            <br>
                            <div v-if="comment.heat <= 999" style="text-align: left; color: gray; font-size: small">
                                {{comment.time}}&nbsp;&nbsp;&nbsp;&nbsp;
                                <el-link :underline="false" class="layui-icon layui-icon-praise"></el-link>
                                &nbsp;{{comment.heat}}
                            </div>
                            <div v-else style="text-align: left; color: gray; font-size: small">
                                {{comment.time}}&nbsp;&nbsp;&nbsp;&nbsp;
                                <el-link :underline="false" class="layui-icon layui-icon-praise"></el-link>
                                &nbsp;999+
                            </div>
                        </el-card>
                        <br>
                        <br>
                    </div>
                </div>
                <div v-if="hasComment" style="text-align: left" class="block">
                    <br>
                    <br>
                    <el-pagination
                        layout="prev, pager, next"
                        @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-size="10"
                        :total="total">
                    </el-pagination>
                    <br>
                    <br>
                </div>
            </el-tab-pane>
            <el-tab-pane label="Sort by time" name="second">Sort by time</el-tab-pane>
        </el-tabs>
        </el-col>
    </el-main>
</el-container>
</template>

<script>
export default {
    name: 'comments',
    inject: ['reload'],
    data() {
        return{
            isLogin: false,
            comments: [],
            activeName: 'first',
            currentPage: 1,
            hasComment: false,
            total: 0,
        };
    },
    created(){
        if(this.$store.state.token){
            this.isLogin = true;
        }
        this.$axios
            .post("/getComments", {
                imageId: this.$route.params.imageId
            })
            .then(resp => {
                if(resp.status === 200){
                    this.comments = resp.data;
                    if(this.comments.length > 0){
                        this.hasComment = true;
                        this.total = this.comments.length;
                    }
                    else{
                        this.hasComment = false;
                    }
                }
            })
    },
    computed:{

    },
    methods: {
        handleClick(tab, event) {
            console.log(tab, event);
        },
        handleCurrentChange(val){
            this.currentPage = val;
        },
        errorNotification(){
            this.$notify({
            type:'error',
            dangerouslyUseHTMLString: true,
            title: 'Request error',
            message: '<strong style="color:teal">Request error, please try again later.</strong>'
            });
        },
    }
}
</script>

<style scoped>
.mySpan{
  color: #009688;
  font-size: larger;
}
</style>