<template>

  <div class="task">

    <h2>
      📝 任务管理
    </h2>


    <!-- ==================== 选择计划 ==================== -->

    <el-card>

      <div class="select-box">

        <span>
          当前计划：
        </span>

        <el-select
          v-model="currentPlan"
          placeholder="请选择学习计划"
          @change="loadTask"
          style="width:300px"
        >

          <el-option
            v-for="item in plans"
            :key="item.id"
            :label="item.title"
            :value="item.id"
          />

        </el-select>

      </div>

    </el-card>


    <!-- ==================== 统计 ==================== -->

    <el-card
      v-if="currentPlan"
      class="statistics"
    >

      <h3>
        任务完成情况
      </h3>

      <el-progress
        :percentage="progress"
      />

      <div class="count">

        <span>
          总任务：
          {{ tasks.length }}
        </span>

        <span>
          已完成：
          {{ finishCount }}
        </span>

        <span>
          未完成：
          {{ tasks.length - finishCount }}
        </span>

      </div>

    </el-card>


    <!-- ==================== 添加按钮 ==================== -->

    <div class="add">

      <el-button
        type="primary"
        @click="openAdd"
      >
        + 新增任务
      </el-button>

    </div>


    <!-- ==================== 任务列表 ==================== -->

    <el-card>

      <el-table
        :data="tasks"
        style="width:100%"
      >

        <el-table-column
          prop="title"
          label="任务"
        />

        <el-table-column
          prop="content"
          label="内容"
        />

        <el-table-column
          prop="taskDate"
          label="日期"
        />

        <el-table-column
          label="状态"
        >

          <template #default="scope">

            <el-tag
              :type="
                scope.row.status === 1
                  ? 'success'
                  : 'info'
              "
            >

              {{
                scope.row.status === 1
                  ? '已完成'
                  : '未完成'
              }}

            </el-tag>

          </template>

        </el-table-column>


        <el-table-column
          label="操作"
          width="220"
        >

          <template #default="scope">

            <el-button
              size="small"
              type="success"
              @click="changeStatus(scope.row)"
            >

              {{
                scope.row.status === 1
                  ? '取消完成'
                  : '完成'
              }}

            </el-button>


            <el-button
              size="small"
              type="danger"
              @click="remove(scope.row.id)"
            >
              删除
            </el-button>

          </template>

        </el-table-column>

      </el-table>


      <el-empty
        v-if="tasks.length === 0"
        description="暂无任务"
      />

    </el-card>


    <!-- ==================== 新增弹窗 ==================== -->

    <el-dialog
      v-model="dialog"
      title="新增任务"
    >

      <el-form>

        <el-form-item
          label="任务标题"
        >

          <el-input
            v-model="form.title"
          />

        </el-form-item>


        <el-form-item
          label="任务内容"
        >

          <el-input
            type="textarea"
            v-model="form.content"
          />

        </el-form-item>


        <el-form-item
          label="完成日期"
        >

          <el-date-picker
            v-model="form.taskDate"
            type="date"
            value-format="YYYY-MM-DD"
          />

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
  getTaskList,
  addTask,
  updateStatus,
  deleteTask
} from '@/api/task'


import {
  getPlanList
} from '@/api/plan'


/* ==================== 学习计划 ==================== */

const plans = ref([])

const currentPlan = ref(null)


/* ==================== 任务 ==================== */

const tasks = ref([])


/* ==================== 弹窗 ==================== */

const dialog = ref(false)


const form = ref({

  title: '',

  content: '',

  taskDate: ''

})


/* ==================== 获取计划 ==================== */

async function loadPlan() {

  try {

    const res =
      await getPlanList()


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
     * 最终都保证 plans 是数组
     */

    if (Array.isArray(data)) {

      plans.value = data

    } else {

      plans.value = []

    }


    /*
     * 如果当前没有选择计划，
     * 默认选择第一个计划
     */

    if (
      plans.value.length > 0 &&
      currentPlan.value === null
    ) {

      currentPlan.value =
        plans.value[0].id

      await loadTask()

    }

  } catch (error) {

    console.error(
      '加载学习计划失败：',
      error
    )

    plans.value = []

    currentPlan.value = null

  }

}


/* ==================== 获取任务 ==================== */

async function loadTask() {

  /*
   * 没有选择计划
   */

  if (
    currentPlan.value === null ||
    currentPlan.value === undefined ||
    currentPlan.value === ''
  ) {

    tasks.value = []

    return

  }


  try {

    const res =
      await getTaskList(
        currentPlan.value
      )


    const data =
      res?.data?.data


    /*
     * 后端返回 null 时，
     * 强制转换为空数组
     */

    if (Array.isArray(data)) {

      tasks.value = data

    } else {

      tasks.value = []

    }

  } catch (error) {

    console.error(
      '加载任务失败：',
      error
    )

    tasks.value = []

  }

}


/* ==================== 打开新增 ==================== */

function openAdd() {

  if (
    currentPlan.value === null ||
    currentPlan.value === undefined ||
    currentPlan.value === ''
  ) {

    ElMessage.warning(
      '请先选择学习计划'
    )

    return

  }


  form.value = {

    title: '',

    content: '',

    taskDate: ''

  }


  dialog.value = true

}


/* ==================== 新增任务 ==================== */

async function submit() {

  if (
    currentPlan.value === null ||
    currentPlan.value === undefined
  ) {

    ElMessage.warning(
      '请先选择学习计划'
    )

    return

  }


  if (
    !form.value.title ||
    !form.value.title.trim()
  ) {

    ElMessage.warning(
      '请输入任务标题'
    )

    return

  }


  try {

    await addTask({

      planId:
        currentPlan.value,

      title:
        form.value.title,

      content:
        form.value.content,

      taskDate:
        form.value.taskDate

    })


    dialog.value = false


    ElMessage.success(
      '任务添加成功'
    )


    await loadTask()

  } catch (error) {

    console.error(
      '新增任务失败：',
      error
    )

    ElMessage.error(
      '任务添加失败，请检查后端服务'
    )

  }

}


/* ==================== 修改状态 ==================== */

async function changeStatus(row) {

  try {

    await updateStatus({

      id:
        row.id,

      status:
        row.status === 1
          ? 0
          : 1

    })


    await loadTask()


  } catch (error) {

    console.error(
      '修改任务状态失败：',
      error
    )

  }

}


/* ==================== 删除 ==================== */

async function remove(id) {

  try {

    await deleteTask(id)

    await loadTask()

    ElMessage.success(
      '任务删除成功'
    )

  } catch (error) {

    console.error(
      '删除任务失败：',
      error
    )

    ElMessage.error(
      '任务删除失败'
    )

  }

}


/* ==================== 已完成数量 ==================== */

const finishCount = computed(() => {

  if (
    !Array.isArray(tasks.value)
  ) {

    return 0

  }


  return tasks.value.filter(
    item =>
      item &&
      item.status === 1
  ).length

})


/* ==================== 完成百分比 ==================== */

const progress = computed(() => {

  if (
    !Array.isArray(tasks.value) ||
    tasks.value.length === 0
  ) {

    return 0

  }


  return Math.round(

    finishCount.value /
    tasks.value.length *
    100

  )

})


/* ==================== 页面加载 ==================== */

onMounted(async () => {

  await loadPlan()

})

</script>


<style scoped>

.task {

  width: 1100px;

  margin: 40px auto;

}


.select-box {

  display: flex;

  align-items: center;

  gap: 20px;

}


.statistics {

  margin-top: 20px;

}


.count {

  margin-top: 20px;

  display: flex;

  justify-content: space-around;

  color: #666;

}


.add {

  margin: 20px 0;

}

</style>