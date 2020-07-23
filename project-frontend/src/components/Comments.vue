<template>
<el-container>
    <el-main>
        <el-col :span="16" :offset="4">
            <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="Sort by heat" name="first">
                <br>
                <div class=" layui-row">
                    <div class=" layui-col-md7" v-for="(comment,i) in comments" :key="i">
                        <el-card class="box-card" shadow="hover">
                            <div slot="header" style="text-align: left">
                                <span class="el-icon-user">&nbsp;&nbsp;{{comment.username}}</span>
                            </div>
                            <div style="text-align: left">
                                {{comment.content}}
                            </div>
                        </el-card>
                    </div>
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
            activeName: 'first'
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
                    this.comments = resp.data
                }
            })
    },
    computed:{

    },
    methods: {
        handleClick(tab, event) {
            console.log(tab, event);
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