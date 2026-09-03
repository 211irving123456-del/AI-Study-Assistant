<template>

<div class="plan">


<h2>
📚 我的学习计划
</h2>



<div class="header">


<el-button
type="primary"
@click="openAdd"
>

+ 新建学习计划

</el-button>


</div>





<el-row
:gutter="20"
>


<el-col

:span="8"

v-for="item in plans"

:key="item.id"

>


<el-card

class="plan-card"

>



<h3>

{{item.title}}

</h3>



<p>

{{item.content}}

</p>




<div class="info">


<p>

📅

{{item.startDate}}

~

{{item.endDate}}

</p>



<p>

⏱目标：

{{item.targetHours}}

小时

</p>


</div>





<el-tag

:type="item.status===1?'success':'info'"

>

{{

item.status===1

?

'已完成'

:

'进行中'

}}

</el-tag>





<div class="button">



<!-- 新增：开始学习 -->

<el-button

size="small"

type="success"

@click="startStudy(item)"

>

开始学习

</el-button>






<el-button

size="small"

type="primary"

@click="openEdit(item)"

>

编辑

</el-button>




<el-button

size="small"

type="danger"

@click="remove(item.id)"

>

删除

</el-button>


</div>



</el-card>


</el-col>


</el-row>






<el-empty

v-if="plans.length===0"

description="暂无学习计划"

/>









<!-- 添加修改弹窗 -->

<el-dialog

v-model="dialog"

:title="editMode?'修改学习计划':'新建学习计划'"

>


<el-form>



<el-form-item

label="计划名称"

>


<el-input

v-model="form.title"

/>


</el-form-item>





<el-form-item

label="学习内容"

>


<el-input

type="textarea"

:rows="3"

v-model="form.content"

/>


</el-form-item>






<el-form-item

label="开始日期"

>


<el-date-picker

v-model="form.startDate"

type="date"

value-format="YYYY-MM-DD"

/>


</el-form-item>






<el-form-item

label="结束日期"

>


<el-date-picker

v-model="form.endDate"

type="date"

value-format="YYYY-MM-DD"

/>


</el-form-item>






<el-form-item

label="目标小时"

>


<el-input-number

v-model="form.targetHours"

:min="1"

/>


</el-form-item>



</el-form>





<template #footer>


<el-button

@click="dialog=false"

>

取消

</el-button>




<el-button

type="primary"

@click="submit"

>

确定

</el-button>


</template>



</el-dialog>





<!-- 学习计时器 -->

<StudyTimer

ref="timerRef"

:plan="currentStudyPlan"

@finish="load"

/>



</div>


</template>





<script setup>


import {

ref,

onMounted

} from 'vue'



import {

getPlanList,

addPlan,

updatePlan,

deletePlan

} from '@/api/plan'



import StudyTimer from '@/components/StudyTimer.vue'






const plans=ref([])



const dialog=ref(false)



const editMode=ref(false)



const timerRef=ref()



// 当前学习计划

const currentStudyPlan=ref({})




const form=ref({

id:null,

title:'',

content:'',

startDate:'',

endDate:'',

targetHours:0

})




async function load(){


const res =

await getPlanList()



plans.value=

res.data.data || []


}





// 开始学习

function startStudy(item){


currentStudyPlan.value=item



timerRef.value.open()



}






function openAdd(){


editMode.value=false



form.value={

id:null,

title:'',

content:'',

startDate:'',

endDate:'',

targetHours:0

}



dialog.value=true


}
// 编辑

function openEdit(item){


editMode.value=true



form.value={


id:item.id,


title:item.title,


content:item.content,


startDate:item.startDate,


endDate:item.endDate,


targetHours:item.targetHours


}



dialog.value=true


}






// 提交新增/修改

async function submit(){



if(editMode.value){


await updatePlan({

id:form.value.id,

title:form.value.title,

content:form.value.content,

startDate:form.value.startDate,

endDate:form.value.endDate,

targetHours:form.value.targetHours


})



}else{


await addPlan({


title:form.value.title,

content:form.value.content,

startDate:form.value.startDate,

endDate:form.value.endDate,

targetHours:form.value.targetHours


})



}



dialog.value=false



load()


}







// 删除计划

async function remove(id){


await deletePlan(id)



load()


}






onMounted(()=>{


load()


})


</script>







<style scoped>


.plan{


width:1100px;

margin:40px auto;


}





.header{


margin-bottom:20px;


}





.plan-card{


height:330px;

margin-bottom:20px;


}





.plan-card h3{


font-size:22px;


}





.info{


color:#666;

margin-top:20px;


}





.button{


margin-top:30px;


}





.button .el-button{


margin-right:10px;


}



</style>