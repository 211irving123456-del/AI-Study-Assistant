<template>

  <div class="study">

    <h2>
      📖 学习记录
    </h2>


    <!-- ==================== 顶部统计 ==================== -->

    <el-card class="summary">

      <div>

        <h3>
          累计学习时长
        </h3>

        <p class="time">
          {{ totalDuration }}
          分钟
        </p>

      </div>


      <div>

        <h3>
          学习次数
        </h3>

        <p class="time">
          {{ records.length }}
          次
        </p>

      </div>

    </el-card>


    <!-- ==================== 添加按钮 ==================== -->

    <div class="add">

      <el-button
        type="primary"
        @click="openAdd"
      >
        + 添加学习记录
      </el-button>

    </div>


    <!-- ==================== 学习记录列表 ==================== -->

    <el-row
      :gutter="20"
    >

      <el-col
        :span="8"
        v-for="item in records"
        :key="item.id"
      >

        <el-card
          class="record-card"
        >

          <h3>
            {{ item.title }}
          </h3>


          <p>
            {{ item.content }}
          </p>


          <div class="duration">

            ⏱
            学习：
            {{ item.duration }}
            分钟

          </div>


          <div class="date">

            创建时间：
            {{ formatTime(item.createTime) }}

          </div>


          <div class="button">

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
      v-if="records.length === 0"
      description="暂无学习记录"
    />


    <!-- ==================== 添加修改窗口 ==================== -->

    <el-dialog
      v-model="dialog"
      :title="
        editMode
          ? '修改学习记录'
          : '新增学习记录'
      "
    >

      <el-form>

        <el-form-item v-if="!editMode" label="所属学习计划">
          <el-select v-model="form.planId" placeholder="请选择学习计划" style="width: 100%">
            <el-option
              v-for="plan in plans"
              :key="plan.id"
              :label="plan.title"
              :value="plan.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          label="标题"
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
            :rows="4"
            v-model="form.content"
          />

        </el-form-item>


        <el-form-item
          label="学习时长"
        >

          <el-input-number
            v-model="form.duration"
            :min="0"
          />

          分钟

        </el-form-item>

      </el-form>


      <template #footer>

        <el-button
          @click="dialog = false"
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

  </div>

</template>


<script setup>

import {
  ref,
  computed,
  onMounted
} from 'vue'


import {
  ElMessage
} from 'element-plus'


import {
  getStudyList,
  addStudy,
  updateStudy,
  deleteStudy
} from '@/api/study'
import { getPlanList } from '@/api/plan'


/* ==================== 数据 ==================== */

const records = ref([])
const plans = ref([])


/* ==================== 弹窗 ==================== */

const dialog = ref(false)

const editMode = ref(false)


/* ==================== 表单 ==================== */

const form = ref({

  id: null,

  planId: null,

  title: '',

  content: '',

  duration: 0

})


/* ==================== 加载数据 ==================== */

async function load() {

  try {

    const res =
      await getStudyList()


    const data =
      res?.data?.data


    /*
     * 无论后端返回：
     *
     * []
     * null
     * undefined
     * 其他类型
     *
     * 最终保证 records 一定是数组
     */

    if (Array.isArray(data)) {

      records.value = data

    } else {

      records.value = []

    }

  } catch (error) {

    console.error(
      '加载学习记录失败：',
      error
    )

    records.value = []

  }

}


async function loadPlans() {
  try {
    const res = await getPlanList()
    plans.value = Array.isArray(res?.data?.data) ? res.data.data : []
  } catch (error) {
    console.error('加载学习计划失败：', error)
    plans.value = []
    ElMessage.error('学习计划加载失败，请稍后重试')
  }
}

/* ==================== 打开新增 ==================== */

async function openAdd() {

  editMode.value = false


  form.value = {

    id: null,

    planId: null,

    title: '',

    content: '',

    duration: 0

  }


  await loadPlans()
  if (plans.value.length === 0) {
    ElMessage.warning('请先创建学习计划，再添加学习记录')
    return
  }
  dialog.value = true

}


/* ==================== 打开编辑 ==================== */

function openEdit(item) {

  editMode.value = true


  form.value = {

    id:
      item.id,

    title:
      item.title || '',

    content:
      item.content || '',

    duration:
      Number(item.duration || 0),

    planId: item.planId ?? null

  }


  dialog.value = true

}


/* ==================== 提交 ==================== */

async function submit() {

  /*
   * 标题不能为空
   */

  if (
    !form.value.title ||
    !form.value.title.trim()
  ) {

    ElMessage.warning(
      '请输入学习记录标题'
    )

    return

  }


  if (!editMode.value && form.value.planId == null) {
    ElMessage.warning('请选择所属学习计划')
    return
  }

  if (!Number.isInteger(Number(form.value.duration)) || Number(form.value.duration) < 1) {
    ElMessage.warning('学习时长必须至少为 1 分钟')
    return
  }

  if (!form.value.content?.trim()) {
    ElMessage.warning('请输入学习内容')
    return
  }

  try {

    /*
     * 修改
     */

    if (editMode.value) {

      await updateStudy({

        id:
          form.value.id,

        title:
          form.value.title,

        content:
          form.value.content,

        duration:
          form.value.duration

      })


      ElMessage.success(
        '学习记录修改成功'
      )

    }

    /*
     * 新增
     */

    else {

      await addStudy({

        planId: form.value.planId,

        title:
          form.value.title,

        content:
          form.value.content,

        duration:
          form.value.duration

      })


      ElMessage.success(
        '学习记录添加成功'
      )

    }


    dialog.value = false


    await load()


  } catch (error) {

    console.error(
      '保存学习记录失败：',
      error
    )

    ElMessage.error(
      '保存失败，请检查后端服务'
    )

  }

}


/* ==================== 删除 ==================== */

async function remove(id) {

  try {

    await deleteStudy(id)


    await load()


    ElMessage.success(
      '学习记录删除成功'
    )

  } catch (error) {

    console.error(
      '删除学习记录失败：',
      error
    )

    ElMessage.error(
      '删除失败'
    )

  }

}


/* ==================== 总时长 ==================== */

const totalDuration = computed(() => {

  if (
    !Array.isArray(records.value)
  ) {

    return 0

  }


  return records.value.reduce(

    (sum, item) => {

      return (
        sum +
        Number(
          item?.duration || 0
        )
      )

    },

    0

  )

})


/* ==================== 时间格式 ==================== */

function formatTime(time) {

  if (!time) {

    return ''

  }


  return String(time)
    .replace('T', ' ')

}


/* ==================== 页面加载 ==================== */

onMounted(() => {

  load()

})

</script>


<style scoped>

.study {

  width: 1100px;

  margin: 40px auto;

}


.summary {

  display: flex;

  justify-content: space-around;

  margin-bottom: 20px;

}


.time {

  font-size: 28px;

  color: #409eff;

}


.add {

  margin-bottom: 20px;

}


.record-card {

  height: 260px;

  margin-bottom: 20px;

}


.record-card p {

  color: #666;

  height: 60px;

}


.duration {

  margin-top: 20px;

  color: #67c23a;

}


.date {

  margin-top: 10px;

  font-size: 13px;

  color: #999;

}


.button {

  margin-top: 25px;

}

</style>