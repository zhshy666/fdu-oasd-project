<template>
<el-container>
    <el-main>
        <el-col :span="16" :offset="4">
            <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="Sort by heat" name="first">
                <br>
                <div v-if="!hasComment">No comments. Write down your comments now!</div>
                <div v-if="hasComment" class=" layui-row">
                    <div class=" layui-col-md7" v-for="(comment,i) in comments.slice((currentPage-1)*6,currentPage*6)" :key="i">
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
                                <el-link v-if="!isLogin" disabled :underline="false" class="layui-icon layui-icon-praise"></el-link>
                                <el-link v-else-if="comment.favor" :underline="false" class="layui-icon layui-icon-praise" style="color: red" v-on:click="cancelLike(comment)"></el-link>
                                <el-link v-else :underline="false" class="layui-icon layui-icon-praise" v-on:click="addLike(comment)"></el-link>
                                &nbsp;{{comment.heat}}
                            </div>
                            <div v-else style="text-align: left; color: gray; font-size: small">
                                {{comment.time}}&nbsp;&nbsp;&nbsp;&nbsp;
                                <el-link v-if="!isLogin" disabled :underline="false" class="layui-icon layui-icon-praise"></el-link>
                                <el-link v-else-if="comment.favor" :underline="false" class="layui-icon layui-icon-praise" style="color: red" v-on:click="cancelLike(comment)"></el-link>
                                <el-link v-else :underline="false" class="layui-icon layui-icon-praise" v-on:click="addLike(comment)"></el-link>
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
                        :page-size="6"
                        :total="total">
                    </el-pagination>
                    <br>
                    <br>
                </div>
            </el-tab-pane>

            <el-tab-pane label="Sort by time" name="second">
                <br>
                <div v-if="!hasComment">No comments. Write down your comments now!</div>
                <div v-if="hasComment" class=" layui-row">
                    <div class=" layui-col-md7" v-for="(comment,i) in commentsByTime.slice((currentPage2-1)*6,currentPage2*6)" :key="i">
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
                                <el-link v-if="!isLogin" disabled :underline="false" class="layui-icon layui-icon-praise"></el-link>
                                <el-link v-else-if="comment.favor" :underline="false" class="layui-icon layui-icon-praise" style="color: red" v-on:click="cancelLike(comment)"></el-link>
                                <el-link v-else :underline="false" class="layui-icon layui-icon-praise" v-on:click="addLike(comment)"></el-link>
                                &nbsp;{{comment.heat}}
                            </div>
                            <div v-else style="text-align: left; color: gray; font-size: small">
                                {{comment.time}}&nbsp;&nbsp;&nbsp;&nbsp;
                                <el-link v-if="!isLogin" disabled :underline="false" class="layui-icon layui-icon-praise"></el-link>
                                <el-link v-else-if="comment.favor" :underline="false" class="layui-icon layui-icon-praise" style="color: red" v-on:click="cancelLike(comment)"></el-link>
                                <el-link v-else :underline="false" class="layui-icon layui-icon-praise" v-on:click="addLike(comment)"></el-link>
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
                        @current-change="handleCurrentChange2"
                        :current-page="currentPage2"
                        :page-size="6"
                        :total="total">
                    </el-pagination>
                    <br>
                    <br>
                </div>
            </el-tab-pane>
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
            commentsByTime: [],
            activeName: 'first',
            currentPage: 1,
            currentPage2: 1,
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
                imageId: this.$route.params.imageId,
                login: this.isLogin,
            })
            .then(resp => {
                if(resp.status === 200){
                    this.comments = resp.data;
                    if(this.comments.length > 0){
                        this.hasComment = true;
                        this.total = this.comments.length;
                        this.comments = resp.data.slice(0, this.total);
                        this.commentsByTime = this.sortByKey(resp.data, 'time');
                    }
                    else{
                        this.hasComment = false;
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
    computed:{

    },
    methods: {
        handleClick(tab, event) {
            console.log(tab, event);
        },
        handleCurrentChange(val){
            this.currentPage = val;
        },
        handleCurrentChange2(val){
            this.currentPage2 = val;
        },
        sortByKey(ary, key) {
            return ary.sort(function (a, b) {
                let x = a[key]
                let y = b[key]
                return ((x < y) ? 1 : (x > y) ? -1 : 0)
            })
        },
        cancelLike(comment){
              this.$axios
                .post("/cancelLike",{
                  commentId: comment.commentId,
                })
                .then(resp => {
                  if(resp.status === 200){
                      this.reload();
                  }
                  else{
                      this.errorNotification();
                  }
                })
                .catch(error => {
                    console.log(error);
                    this.errorNotification();
                });
        },
        addLike(comment){
            this.$axios
                .post("/addLike",{
                  commentId: comment.commentId,
                })
                .then(resp => {
                  if(resp.status === 200){
                      this.reload();
                  }
                  else{
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
    }
}
</script>

<style scoped>
.mySpan{
  color: #009688;
  font-size: larger;
}
</style>