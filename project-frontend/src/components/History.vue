<template>
<div>
    <el-container>
        <el-main>
            <el-col :span="24" v-if="hasHistory">
                <el-table
                    :data="images"
                    stripe
                    style="width: 100%">
                    <el-table-column
                    type="index"
                    width="50">
                    </el-table-column>
                    <el-table-column
                    label="Title"
                    width="270">
                    <template slot-scope="scope">
                        <router-link :to="'imageDetail/'+scope.row.imageId">{{ scope.row.title }}</router-link>
                    </template>
                    </el-table-column>

                    <el-table-column
                    prop="author"
                    label="Author"
                    width="200">
                    </el-table-column>
                    <el-table-column
                    prop="content"
                    label="Content"
                    width="150">
                    </el-table-column>
                    <el-table-column
                    prop="releasedTime"
                    label="Released on"
                    width="240">
                    </el-table-column>
                    <el-table-column
                    prop="heat"
                    label="Heat"
                    width="100">
                    </el-table-column>
                </el-table>
                <div style="font-size: small">
                    <br>
                    <br>
                    10 images you recently viewed at most will be shown on this page.
                </div>
            </el-col>
            <el-col :span="24" v-if="noHistory">
                No history. Click <router-link :to="'/'">here</router-link> to view images uploaded by other users.
            </el-col>
        </el-main>
    </el-container>
</div>
</template>

<script>
export default {
    name: "history",
    data() {
        return{
            hasHistory: false,
            noHistory: true,
            images: [],
        }
    },
    created() {
        this.$axios
            .get("/getHistory", {})
            .then(resp => {
                if(resp.status === 200){
                    this.images = resp.data;
                    if(this.images.length > 0){
                        this.hasHistory = true;
                        this.noHistory = false;
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
        goto(row){
            console.log(row);
            // this.$router.replace({ path: "/imageDetail/"+image.imageId });
        },
        errorNotification(){
            this.$message.error('Requset error, please try again');
        },
    }
}
</script>

<style scoped>

</style>