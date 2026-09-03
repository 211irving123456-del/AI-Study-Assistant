<template>

  <div class="home">

    <h1>
      👋 欢迎回来，开始今天的学习吧！
    </h1>


    <!-- ==================== 数据卡片 ==================== -->

    <el-row :gutter="20">


      <!-- 学习计划 -->

      <el-col :span="6">

        <el-card>

          <h3>
            📚 学习计划
          </h3>

          <div class="number">
            {{ planCount }}
          </div>

          <p>
            个计划
          </p>

        </el-card>

      </el-col>


      <!-- 学习记录 -->

      <el-col :span="6">

        <el-card>

          <h3>
            📝 学习记录
          </h3>

          <div class="number">
            {{ recordCount }}
          </div>

          <p>
            次学习
          </p>

        </el-card>

      </el-col>


      <!-- 学习时长 -->

      <el-col :span="6">

        <el-card>

          <h3>
            ⏱ 学习时长
          </h3>

          <div class="number">
            {{ totalDuration }}
          </div>

          <p>
            分钟
          </p>

        </el-card>

      </el-col>


      <!-- 任务完成率 -->

      <el-col :span="6">

        <el-card>

          <h3>
            ✅ 任务完成率
          </h3>

          <div class="number">
            {{ finishRate }}%
          </div>

          <p>
            {{ finishTask }}/{{ taskCount }}
          </p>

        </el-card>

      </el-col>

    </el-row>


    <!-- ==================== 快捷入口 ==================== -->

    <el-card class="quick">

      <h2>
        🚀 快捷功能
      </h2>


      <el-button
        type="primary"
        @click="goChat"
      >
        AI学习助手
      </el-button>


      <el-button
        type="success"
        @click="goPlan"
      >
        学习计划
      </el-button>


      <el-button
        @click="goStudy"
      >
        学习记录
      </el-button>

    </el-card>


    <!-- ==================== 图表 ==================== -->

    <div class="charts">


      <!-- 任务完成情况 -->

      <el-card>

        <h2>
          任务完成情况
        </h2>

        <div
          id="taskChart"
          class="chart"
        />

      </el-card>


      <!-- 学习时长统计 -->

      <el-card>

        <h2>
          学习时长统计
        </h2>

        <div
          id="durationChart"
          class="chart"
        />

      </el-card>

    </div>


    <!-- ==================== 最近任务 ==================== -->

    <el-card>

      <h2>
        📌 最近任务
      </h2>


      <el-table
        :data="recentTasks"
        empty-text="暂无任务"
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
                  ? '完成'
                  : '未完成'
              }}

            </el-tag>

          </template>

        </el-table-column>

      </el-table>

    </el-card>


    <!-- ==================== 最近学习记录 ==================== -->

    <el-card
      style="margin-top:20px"
    >

      <h2>
        📖 最近学习记录
      </h2>


      <el-table
        :data="records"
        empty-text="暂无学习记录"
      >

        <el-table-column
          prop="title"
          label="标题"
        />


        <el-table-column
          prop="duration"
          label="时长(分钟)"
        />


        <el-table-column
          prop="content"
          label="内容"
        />

      </el-table>

    </el-card>

  </div>

</template>


<script setup>

import {
  ref,
  onMounted,
  nextTick,
  onBeforeUnmount
} from 'vue'


import {
  useRouter
} from 'vue-router'


import * as echarts from 'echarts'


import {
  ElMessage
} from 'element-plus'


import request from '@/api/request'


/* ==================== 路由 ==================== */

const router =
  useRouter()


/* ==================== 数据 ==================== */

const planCount =
  ref(0)


const recordCount =
  ref(0)


const totalDuration =
  ref(0)


/*
 * 这三个必须永远是数组
 */

const records =
  ref([])


const plans =
  ref([])


const tasks =
  ref([])


const recentTasks =
  ref([])


const taskCount =
  ref(0)


const finishTask =
  ref(0)


const finishRate =
  ref(0)


/* ==================== 图表 ==================== */

let taskChartInstance =
  null


let durationChartInstance =
  null


/* ==================== 快捷按钮 ==================== */

function goChat() {

  router.push('/chat')

}


function goPlan() {

  router.push('/plan')

}


function goStudy() {

  router.push('/study')

}


/* ==================== 加载学习记录 ==================== */

async function loadRecord() {

  try {

    const res =
      await request.get(
        '/study/list'
      )


    const data =
      res?.data?.data


    /*
     * 强制保证 records 是数组
     */

    records.value =
      Array.isArray(data)
        ? data
        : []


    recordCount.value =
      records.value.length


    totalDuration.value =
      records.value.reduce(

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

  } catch (error) {

    console.error(
      '加载学习记录失败：',
      error
    )


    records.value = []

    recordCount.value = 0

    totalDuration.value = 0

  }

}


/* ==================== 加载学习计划 ==================== */

async function loadPlan() {

  try {

    const res =
      await request.get(
        '/plan/list'
      )


    const data =
      res?.data?.data


    plans.value =
      Array.isArray(data)
        ? data
        : []


    planCount.value =
      plans.value.length


  } catch (error) {

    console.error(
      '加载学习计划失败：',
      error
    )


    plans.value = []

    planCount.value = 0

  }

}


/* ==================== 加载任务 ==================== */

async function loadTask() {

  try {

    const result = []


    /*
     * 没有计划
     */

    if (
      !Array.isArray(plans.value) ||
      plans.value.length === 0
    ) {

      tasks.value = []

      recentTasks.value = []

      taskCount.value = 0

      finishTask.value = 0

      finishRate.value = 0

      return

    }


    /*
     * 查询所有计划的任务
     */

    for (
      const p of plans.value
    ) {

      /*
       * 防止异常计划数据
       */

      if (
        !p ||
        p.id === null ||
        p.id === undefined
      ) {

        continue

      }


      try {

        const res =
          await request.get(
            '/task/list/' +
            p.id
          )


        const data =
          res?.data?.data


        /*
         * 只有数组才加入结果
         */

        if (
          Array.isArray(data)
        ) {

          result.push(
            ...data
          )

        }

      } catch (error) {

        console.error(
          `加载计划 ${p.id} 的任务失败：`,
          error
        )

      }

    }


    /*
     * 最终保证 tasks 是数组
     */

    tasks.value =
      Array.isArray(result)
        ? result
        : []


    taskCount.value =
      tasks.value.length


    finishTask.value =
      tasks.value.filter(
        item =>
          item &&
          item.status === 1
      ).length


    if (
      taskCount.value > 0
    ) {

      finishRate.value =
        Math.round(

          finishTask.value /
          taskCount.value *
          100

        )

    } else {

      finishRate.value = 0

    }


    recentTasks.value =
      tasks.value.slice(0, 5)


  } catch (error) {

    console.error(
      '加载任务失败：',
      error
    )


    tasks.value = []

    recentTasks.value = []

    taskCount.value = 0

    finishTask.value = 0

    finishRate.value = 0

  }

}


/* ==================== 任务饼图 ==================== */

function initTaskChart() {

  const dom =
    document.getElementById(
      'taskChart'
    )


  if (!dom) {

    return

  }


  if (
    taskChartInstance
  ) {

    taskChartInstance.dispose()

  }


  taskChartInstance =
    echarts.init(dom)


  const unfinished =
    Math.max(

      taskCount.value -
      finishTask.value,

      0

    )


  if (
    taskCount.value === 0
  ) {

    taskChartInstance.setOption({

      title: {

        text:
          '暂无任务',

        left:
          'center',

        top:
          'center',

        textStyle: {

          fontSize:
            18

        }

      },

      series: []

    })

    return

  }


  taskChartInstance.setOption({

    tooltip: {},

    series: [

      {

        type:
          'pie',

        radius:
          '60%',

        data: [

          {

            value:
              finishTask.value,

            name:
              '已完成'

          },

          {

            value:
              unfinished,

            name:
              '未完成'

          }

        ]

      }

    ]

  })

}


/* ==================== 学习时长图 ==================== */

function initDurationChart() {

  const dom =
    document.getElementById(
      'durationChart'
    )


  if (!dom) {

    return

  }


  if (
    durationChartInstance
  ) {

    durationChartInstance.dispose()

  }


  durationChartInstance =
    echarts.init(dom)


  if (
    !Array.isArray(records.value) ||
    records.value.length === 0
  ) {

    durationChartInstance.setOption({

      title: {

        text:
          '暂无学习记录',

        left:
          'center',

        top:
          'center',

        textStyle: {

          fontSize:
            18

        }

      },

      xAxis: {

        show:
          false

      },

      yAxis: {

        show:
          false

      },

      series: []

    })

    return

  }


  durationChartInstance.setOption({

    tooltip: {},

    xAxis: {

      type:
        'category',

      data:
        records.value.map(

          item =>
            item?.title ||
            '未命名'

        )

    },

    yAxis: {

      type:
        'value'

    },

    series: [

      {

        type:
          'bar',

        data:
          records.value.map(

            item =>
              Number(
                item?.duration || 0
              )

          )

      }

    ]

  })

}


/* ==================== 初始化 ==================== */

async function init() {

  /*
   * 先加载学习记录
   */

  await loadRecord()


  /*
   * 加载计划
   */

  await loadPlan()


  /*
   * 根据计划加载任务
   */

  await loadTask()


  /*
   * 等待 Vue 更新 DOM
   */

  await nextTick()


  /*
   * 初始化图表
   */

  initTaskChart()

  initDurationChart()

}


/* ==================== 浏览器窗口变化 ==================== */

function handleResize() {

  if (
    taskChartInstance
  ) {

    taskChartInstance.resize()

  }


  if (
    durationChartInstance
  ) {

    durationChartInstance.resize()

  }

}


/* ==================== 页面加载 ==================== */

onMounted(() => {

  init()


  window.addEventListener(
    'resize',
    handleResize
  )

})


/* ==================== 页面销毁 ==================== */

onBeforeUnmount(() => {

  window.removeEventListener(
    'resize',
    handleResize
  )


  if (
    taskChartInstance
  ) {

    taskChartInstance.dispose()

    taskChartInstance = null

  }


  if (
    durationChartInstance
  ) {

    durationChartInstance.dispose()

    durationChartInstance = null

  }

})

</script>


<style scoped>

.home {

  padding: 30px;

}


.number {

  font-size: 38px;

  font-weight: bold;

  color: #409eff;

}


.quick {

  margin: 20px 0;

}


.charts {

  display: flex;

  gap: 20px;

  margin: 20px 0;

}


.charts .el-card {

  flex: 1;

}


.chart {

  height: 300px;

}

</style>