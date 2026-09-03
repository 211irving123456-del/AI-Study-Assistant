<template>

  <div class="chat-page">

    <h2>
      🤖 AI学习助手
    </h2>


    <el-card class="chat-box">


      <!-- ==================== 聊天消息 ==================== -->

      <div class="messages">

        <div
          v-for="item in messages"
          :key="item.id"
          :class="
            item.type === 'user'
              ? 'user-msg'
              : 'ai-msg'
          "
        >

          <div class="bubble">

            <b v-if="item.type === 'user'">
              我：
            </b>

            <b v-else>
              AI：
            </b>

            <div class="message-content">
              {{ item.content }}
            </div>

          </div>

        </div>


        <!-- AI思考中 -->

        <div
          v-if="loading"
          class="ai-msg"
        >

          <div class="bubble">
            AI正在思考...
          </div>

        </div>

      </div>


      <!-- ==================== 输入区域 ==================== -->

      <div class="input-area">

        <el-input
          v-model="message"
          placeholder="请输入学习问题..."
          @keyup.enter="send"
          :disabled="loading"
        />


        <el-button
          type="primary"
          @click="send"
          :loading="loading"
        >
          发送
        </el-button>


        <el-button
          type="success"
          @click="openGenerate"
          :disabled="loading"
        >
          AI生成学习计划
        </el-button>

      </div>

    </el-card>


    <!-- ==================== AI生成计划 ==================== -->

    <el-dialog
      v-model="generateDialog"
      title="AI生成学习计划"
      width="600px"
    >

      <el-input
        type="textarea"
        :rows="5"
        v-model="requirement"
        placeholder="例如：我想三个月学会Java后端开发"
      />


      <template #footer>

        <el-button
          @click="generateDialog = false"
          :disabled="generating"
        >
          取消
        </el-button>


        <el-button
          type="primary"
          @click="generate"
          :loading="generating"
        >
          生成
        </el-button>

      </template>

    </el-dialog>


    <!-- ==================== AI计划生成结果 ==================== -->

    <el-dialog
      v-model="planResultDialog"
      title="📚 AI生成的学习计划"
      width="750px"
    >

      <div class="plan-result">

        <h3>
          {{ generatedPlan.title }}
        </h3>


        <div class="target-hours">

          🎯 预计学习时长：
          {{ generatedPlan.targetHours }}
          小时

        </div>


        <div class="plan-content">

          {{ generatedPlan.content }}

        </div>

      </div>


      <template #footer>

        <el-button
          type="primary"
          @click="planResultDialog = false"
        >
          知道了
        </el-button>

      </template>

    </el-dialog>

  </div>

</template>


<script setup>

import {
  ref,
  onMounted,
  nextTick
} from 'vue'


import {
  ElMessage
} from 'element-plus'


import {
  chat,
  getHistory,
  generatePlan
} from '@/api/chat'


/* ==================== 聊天 ==================== */

const message = ref('')

const messages = ref([])

const loading = ref(false)


/* ==================== AI生成计划 ==================== */

const generateDialog = ref(false)

const generating = ref(false)

const requirement = ref('')


/* ==================== 计划结果 ==================== */

const planResultDialog = ref(false)


const generatedPlan = ref({

  title: '',

  content: '',

  targetHours: 0

})


/* ==================== 打开生成计划 ==================== */

function openGenerate() {

  requirement.value = ''

  generateDialog.value = true

}


/* ==================== 发送聊天 ==================== */

async function send() {

  const text =
    message.value.trim()


  if (!text) {

    ElMessage.warning(
      '请输入学习问题'
    )

    return

  }


  if (loading.value) {

    return

  }


  /*
   * 添加用户消息
   */

  messages.value.push({

    id:
      Date.now() + '-user',

    type:
      'user',

    content:
      text

  })


  message.value = ''

  loading.value = true


  try {

    const res =
      await chat({

        message:
          text

      })


    const answer =
      res?.data?.data


    /*
     * AI返回为空
     */

    if (
      answer === null ||
      answer === undefined ||
      answer === ''
    ) {

      messages.value.push({

        id:
          Date.now() + '-ai',

        type:
          'ai',

        content:
          'AI暂时没有返回有效内容，请稍后再试。'

      })

    }

    else {

      messages.value.push({

        id:
          Date.now() + '-ai',

        type:
          'ai',

        content:
          String(answer)

      })

    }


    await nextTick()

    scrollToBottom()


  } catch (error) {

    console.error(
      'AI聊天请求失败：',
      error
    )


    messages.value.push({

      id:
        Date.now() + '-error',

      type:
        'ai',

      content:
        '⚠️ AI服务暂时无法响应，请检查后端服务或稍后重试。'

    })


    ElMessage.error(
      'AI请求失败，请检查后端服务'
    )

  } finally {

    loading.value = false

  }

}


/* ==================== 加载历史记录 ==================== */

async function load() {

  try {

    const res =
      await getHistory()


    /*
     * 获取后端 data
     */

    const data =
      res?.data?.data


    /*
     * 只有真正的数组才能 flatMap
     */

    const list =
      Array.isArray(data)
        ? data
        : []


    /*
     * 如果没有历史记录
     */

    if (list.length === 0) {

      messages.value = []

      return

    }


    /*
     * 转换成聊天消息
     */

    messages.value =
      list.flatMap(
        item => [

          {

            id:
              String(item.id) +
              '-user',

            type:
              'user',

            content:
              item?.question || ''

          },


          {

            id:
              String(item.id) +
              '-ai',

            type:
              'ai',

            content:
              item?.answer || ''

          }

        ]
      )


    await nextTick()

    scrollToBottom()


  } catch (error) {

    console.error(
      '加载聊天历史失败：',
      error
    )


    messages.value = []

  }

}


/* ==================== 生成学习计划 ==================== */

async function generate() {

  const text =
    requirement.value.trim()


  if (!text) {

    ElMessage.warning(
      '请输入你的学习需求'
    )

    return

  }


  if (generating.value) {

    return

  }


  generating.value = true


  try {

    const res =
      await generatePlan({

        requirement:
          text

      })


    const data =
      res?.data?.data


    if (
      !data ||
      typeof data !== 'object'
    ) {

      ElMessage.error(
        'AI没有返回有效学习计划'
      )

      return

    }


    generatedPlan.value = {

      title:
        data.title ||
        'AI生成学习计划',

      content:
        data.content ||
        'AI没有返回计划内容',

      targetHours:
        Number(
          data.targetHours || 0
        )

    }


    /*
     * 在聊天窗口添加消息
     */

    messages.value.push({

      id:
        Date.now() + '-plan',

      type:
        'ai',

      content:
        '📚 已经根据你的需求生成学习计划。\n\n' +
        '计划名称：' +
        generatedPlan.value.title +
        '\n\n' +
        '预计学习时长：' +
        generatedPlan.value.targetHours +
        ' 小时\n\n' +
        '学习计划已经保存到【学习计划】模块。'

    })


    generateDialog.value = false

    requirement.value = ''

    planResultDialog.value = true


    ElMessage.success(
      'AI学习计划生成成功，已保存到学习计划'
    )


    await nextTick()

    scrollToBottom()


  } catch (error) {

    console.error(
      'AI生成学习计划失败：',
      error
    )


    ElMessage.error(
      'AI生成学习计划失败，请检查后端服务'
    )

  } finally {

    generating.value = false

  }

}


/* ==================== 滚动到底部 ==================== */

function scrollToBottom() {

  const element =
    document.querySelector(
      '.messages'
    )


  if (!element) {

    return

  }


  element.scrollTop =
    element.scrollHeight

}


/* ==================== 页面加载 ==================== */

onMounted(() => {

  load()

})

</script>


<style scoped>

.chat-page {

  width: 900px;

  margin: 40px auto;

}


.chat-box {

  height: 700px;

  display: flex;

  flex-direction: column;

}


.messages {

  height: 560px;

  overflow-y: auto;

  padding: 20px;

}


.user-msg {

  display: flex;

  justify-content: flex-end;

  margin: 15px;

}


.ai-msg {

  display: flex;

  justify-content: flex-start;

  margin: 15px;

}


.bubble {

  max-width: 70%;

  padding: 15px;

  border-radius: 10px;

  background: #f2f3f5;

  white-space: pre-wrap;

  line-height: 1.7;

}


.message-content {

  margin-top: 5px;

  white-space: pre-wrap;

}


.user-msg .bubble {

  background: #409eff;

  color: white;

}


.input-area {

  display: flex;

  gap: 10px;

  padding: 20px;

}


.input-area .el-input {

  flex: 1;

}


.plan-result {

  max-height: 550px;

  overflow-y: auto;

  padding: 10px;

}


.plan-result h3 {

  font-size: 22px;

  margin-top: 0;

}


.target-hours {

  padding: 12px;

  margin-bottom: 20px;

  background: #f0f9ff;

  border-radius: 8px;

  color: #409eff;

}


.plan-content {

  white-space: pre-wrap;

  line-height: 1.8;

  font-size: 15px;

}

</style>