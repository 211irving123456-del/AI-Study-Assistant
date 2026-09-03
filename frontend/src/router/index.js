import {
createRouter,
createWebHistory
}
from 'vue-router'


import Login from '@/views/Login.vue'

import Layout from '@/layout/Layout.vue'


import Home from '@/views/Home.vue'

import Plan from '@/views/Plan.vue'

import Task from '@/views/Task.vue'

import StudyRecord from '@/views/StudyRecord.vue'

import Chat from '@/views/Chat.vue'





const router=createRouter({

history:createWebHistory(),


routes:[



{
path:'/login',
component:Login
},



{

path:'/',

component:Layout,


children:[


{
path:'home',
component:Home
},



{
path:'plan',
component:Plan
},



{
path:'task',
component:Task
},



{
path:'study',
component:StudyRecord
},



{
path:'chat',
component:Chat
}



]

}


]


})



export default router