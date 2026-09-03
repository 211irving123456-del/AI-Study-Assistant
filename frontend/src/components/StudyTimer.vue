<template>

<div>


    <el-dialog
        v-model="visible"
        title="学习计时"
        width="420px"
        :close-on-click-modal="false"
    >


        <div class="timer">


            <h1>
                {{ formatTime }}
            </h1>


            <h3>
                {{ plan?.title }}
            </h3>


            <el-button
                v-if="!running"
                type="primary"
                size="large"
                @click="start"
            >
                开始学习
            </el-button>



            <el-button
                v-if="running"
                type="danger"
                size="large"
                @click="pause"
            >
                暂停
            </el-button>



        </div>


    </el-dialog>




    <!-- 学习结束记录 -->

    <el-dialog
        v-model="recordVisible"
        title="完成学习记录"
        width="500px"
    >


        <el-form>


            <el-form-item label="学习标题">

                <el-input
                    v-model="record.title"
                />

            </el-form-item>



            <el-form-item label="学习内容">

                <el-input
                    type="textarea"
                    :rows="4"
                    v-model="record.content"
                />

            </el-form-item>



        </el-form>



        <template #footer>


            <el-button
                @click="recordVisible=false"
            >
                取消
            </el-button>


            <el-button
                type="primary"
                @click="submitRecord"
            >
                保存
            </el-button>


        </template>


    </el-dialog>


</div>


</template>




<script setup>


import {
    ref,
    computed,
    onUnmounted
} from "vue"



import {
    addStudyRecord
} from "@/api/study"



const props = defineProps({

    plan:{
        type:Object,
        default:null
    }

})





const emit = defineEmits([
    "finish"
])





const visible = ref(false)


const recordVisible = ref(false)



const running = ref(false)



const seconds = ref(0)



let timer=null






const record = ref({

    title:"",

    content:""

})







const formatTime = computed(()=>{


    let h=Math.floor(seconds.value/3600)


    let m=Math.floor(
        (seconds.value%3600)/60
    )


    let s=
    seconds.value%60



    return `${String(h).padStart(2,'0')}:${String(m).padStart(2,'0')}:${String(s).padStart(2,'0')}`


})









function open(){


    visible.value=true


}







function start(){


    if(running.value)
        return



    running.value=true



    timer=setInterval(()=>{


        seconds.value++


    },1000)


}







function pause(){


    running.value=false


    clearInterval(timer)



    finish()


}








function finish(){


    clearInterval(timer)


    running.value=false



    record.value={

        title:
        props.plan?.title || "",


        content:""

    }



    visible.value=false



    recordVisible.value=true


}








async function submitRecord(){


    await addStudyRecord({

        planId:
        props.plan.id,


        title:
        record.value.title,


        content:
        record.value.content,


        duration:
        Math.floor(seconds.value/60) || 1


    })



    recordVisible.value=false



    seconds.value=0



    emit("finish")


}








defineExpose({

    open

})








onUnmounted(()=>{


    clearInterval(timer)


})





</script>






<style scoped>


.timer{

    text-align:center;

}


.timer h1{

    font-size:48px;

    margin:20px;

}



</style>