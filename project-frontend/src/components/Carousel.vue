<template>
<el-container>
    <el-main>
        <el-col span="20" offset="2">

  <el-carousel :interval="4000" type="card" height="350px">
    <el-carousel-item v-for="item in 5" :key="item">
      <h3 class="medium">{{ item }}</h3>
    </el-carousel-item>
  </el-carousel>
        </el-col>
    </el-main>
</el-container>
</template>

<style>
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }
  
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  
  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>