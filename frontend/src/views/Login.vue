<script setup>


import {
ref
} from 'vue'


import {
login,
register
} from '@/api/user'


import {
useRouter
} from 'vue-router'


import {
ElMessage
} from 'element-plus'



const router =
useRouter()



// true 登录 false 注册

const isLogin =
ref(true)



const username =
ref('')


const password =
ref('')


const email =
ref('')





// 登录

async function handleLogin(){



if(!username.value || !password.value){

ElMessage.warning(
"请输入用户名和密码"
)

return

}




try{


const res =
await login({

username:
username.value,


password:
password.value


})



console.log(
"登录结果:",
res.data
)



if(res.data.code===200){



localStorage.setItem(

'token',

res.data.data

)




ElMessage.success(
"登录成功"
)



router.push('/home')


}



}catch(e){


ElMessage.error(
"登录失败"
)


}


}











// 注册

async function handleRegister(){



if(
!username.value ||
!password.value
){


ElMessage.warning(
"用户名密码不能为空"
)


return

}



try{


const res =
await register({

username:
username.value,


password:
password.value,


email:
email.value


})



console.log(
"注册结果:",
res.data
)




if(res.data.code===200){


ElMessage.success(
"注册成功，请登录"
)



isLogin.value=true



password.value=''


}



}catch(e){



ElMessage.error(
"注册失败"
)


}



}







</script>






<template>


<div class="login">



<el-card>


<h1>
🤖 AI学习助手
</h1>



<h2>

{{isLogin?'用户登录':'用户注册'}}

</h2>





<el-input

v-model="username"

placeholder="用户名"

/>



<br><br>




<el-input

v-model="password"

type="password"

placeholder="密码"

/>



<br><br>





<el-input

v-if="!isLogin"

v-model="email"

placeholder="邮箱"

/>



<br
v-if="!isLogin"
/>

<br
v-if="!isLogin"
/>





<el-button

type="primary"

style="width:100%"

@click="
isLogin?
handleLogin():
handleRegister()
"

>


{{isLogin?'登录':'注册'}}


</el-button>






<div class="change">


<span

@click="
isLogin=false
"

v-if="isLogin"

>


没有账号？去注册


</span>




<span

@click="
isLogin=true
"

v-else

>


已有账号？去登录


</span>



</div>



</el-card>



</div>


</template>






<style scoped>


.login{


width:400px;


margin:150px auto;


}



.change{


margin-top:20px;


color:#409eff;


cursor:pointer;


text-align:center;


}



</style>