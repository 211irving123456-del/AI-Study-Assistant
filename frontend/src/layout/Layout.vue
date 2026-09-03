<template>


<div class="layout">


<!-- 左侧菜单 -->


<div class="aside">


<h2>
🤖 AI学习助手
</h2>



<el-menu

router

background-color="#001529"

text-color="#fff"

active-text-color="#409eff"

>


<el-menu-item
index="/home"
>

首页

</el-menu-item>



<el-menu-item
index="/plan"
>

学习计划

</el-menu-item>



<el-menu-item
index="/task"
>

任务管理

</el-menu-item>




<el-menu-item
index="/study"
>

学习记录

</el-menu-item>




<el-menu-item
index="/chat"
>

AI聊天

</el-menu-item>



</el-menu>



</div>






<!-- 右侧 -->


<div class="main">



<!-- 顶部 -->

<div class="header">


<div>

AI学习助手系统

</div>


<div>


<span>
{{username}}
</span>



<el-button
type="danger"
size="small"
@click="logout"
>

退出

</el-button>


</div>


</div>






<!-- 页面 -->


<div class="content">


<router-view />

</div>




</div>



</div>



</template>





<script setup>


import {
ref
}
from 'vue'


import {
useRouter
}
from 'vue-router'



const router =
useRouter()



const username =
ref(
localStorage.getItem('username')||'用户'
)






function logout(){



localStorage.removeItem(
'token'
)


router.push(
'/login'
)


}



</script>







<style scoped>


.layout{


display:flex;

height:100vh;

background:#f5f7fa;


}



.aside{


width:220px;

background:#001529;

color:white;


}



.aside h2{


padding:20px;

text-align:center;


}




.main{


flex:1;


}




.header{


height:60px;

background:white;

display:flex;

justify-content:space-between;

align-items:center;

padding:0 30px;

box-shadow:0 2px 8px #ddd;


}



.content{


padding:20px;


}




</style>