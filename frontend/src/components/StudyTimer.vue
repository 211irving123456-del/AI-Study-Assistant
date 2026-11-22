<template>
  <div>
    <el-dialog
      v-model="visible"
      title="学习计时"
      width="420px"
      :close-on-click-modal="false"
      @close="pause"
    >
      <div class="timer">
        <h1>{{ formatTime }}</h1>
        <h3>{{ plan?.title }}</h3>
        <p class="hint">暂停不会结束学习；点击“结束学习”后才会保存记录。</p>

        <div class="actions">
          <el-button v-if="!running" type="primary" size="large" @click="start">
            {{ seconds > 0 ? '继续学习' : '开始学习' }}
          </el-button>
          <el-button v-else type="warning" size="large" @click="pause">
            暂停
          </el-button>
          <el-button type="success" size="large" :disabled="seconds < 60" @click="finish">
            结束学习
          </el-button>
        </div>
        <p v-if="seconds < 60" class="hint">学习满 1 分钟后可以结束并保存记录。</p>
      </div>
    </el-dialog>

    <el-dialog
      v-model="recordVisible"
      title="完成学习记录"
      width="500px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <el-form>
        <el-form-item label="学习标题">
          <el-input v-model="record.title" />
        </el-form-item>
        <el-form-item label="学习内容">
          <el-input v-model="record.content" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="本次时长">
          {{ Math.floor(seconds / 60) }} 分钟
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button :disabled="saving" @click="backToTimer">返回计时器</el-button>
        <el-button type="primary" :loading="saving" @click="submitRecord">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { addStudyRecord } from '@/api/study'

const props = defineProps({
  plan: { type: Object, default: null }
})
const emit = defineEmits(['finish'])

const visible = ref(false)
const recordVisible = ref(false)
const running = ref(false)
const seconds = ref(0)
const saving = ref(false)
const record = ref({ title: '', content: '' })

let interval = null
let startedAt = 0
let secondsBeforeStart = 0
let currentPlanId = null

const formatTime = computed(() => {
  const h = Math.floor(seconds.value / 3600)
  const m = Math.floor((seconds.value % 3600) / 60)
  const s = seconds.value % 60
  return [h, m, s].map(v => String(v).padStart(2, '0')).join(':')
})

function updateElapsed() {
  if (running.value) {
    seconds.value = secondsBeforeStart + Math.floor((Date.now() - startedAt) / 1000)
  }
}

function stopInterval() {
  if (interval !== null) {
    clearInterval(interval)
    interval = null
  }
}

function open() {
  if (!props.plan?.id) {
    ElMessage.warning('请先选择学习计划')
    return
  }
  if (currentPlanId !== props.plan.id) {
    pause()
    seconds.value = 0
    secondsBeforeStart = 0
    currentPlanId = props.plan.id
  }
  visible.value = true
}

function start() {
  if (running.value || !visible.value) return
  secondsBeforeStart = seconds.value
  startedAt = Date.now()
  running.value = true
  stopInterval()
  interval = setInterval(updateElapsed, 250)
}

function pause() {
  if (running.value) updateElapsed()
  running.value = false
  stopInterval()
}

function finish() {
  pause()
  if (seconds.value < 60) {
    ElMessage.warning('至少学习 1 分钟才能保存记录')
    return
  }
  record.value = { title: props.plan?.title || '', content: '' }
  visible.value = false
  recordVisible.value = true
}

function backToTimer() {
  recordVisible.value = false
  visible.value = true
}

async function submitRecord() {
  if (saving.value) return
  if (!record.value.title.trim() || !record.value.content.trim()) {
    ElMessage.warning('请填写学习标题和学习内容')
    return
  }
  if (seconds.value < 60 || currentPlanId == null) {
    ElMessage.warning('学习时长不足或未选择学习计划')
    return
  }
  saving.value = true
  try {
    const response = await addStudyRecord({
      planId: currentPlanId,
      title: record.value.title.trim(),
      content: record.value.content.trim(),
      duration: Math.floor(seconds.value / 60)
    })
    if (response?.data?.code !== 200) {
      throw new Error(response?.data?.message || '保存失败')
    }
    ElMessage.success('学习记录已保存')
    recordVisible.value = false
    seconds.value = 0
    secondsBeforeStart = 0
    currentPlanId = null
    emit('finish')
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error?.message || '保存失败，请重试')
  } finally {
    saving.value = false
  }
}

defineExpose({ open })
onUnmounted(stopInterval)
</script>

<style scoped>
.timer { text-align: center; }
.timer h1 { font-size: 48px; margin: 20px; font-variant-numeric: tabular-nums; }
.actions { display: flex; justify-content: center; gap: 8px; margin: 20px 0; }
.hint { font-size: 13px; color: #777; }
</style>
