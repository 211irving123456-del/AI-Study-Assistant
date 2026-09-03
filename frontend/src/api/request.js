import axios from 'axios'

import {
  ElMessage
} from 'element-plus'


/* ================================ */
/* 创建 axios 实例 */
/* ================================ */

const request = axios.create({

  /*
   * Spring Boot 后端
   */

  baseURL:
    'http://localhost:8080',

  /*
   * AI 请求可能比较慢
   */

  timeout:
    120000

})


/* ================================ */
/* 请求拦截器 */
/* ================================ */

request.interceptors.request.use(

  config => {

    const token =
      localStorage.getItem(
        'token'
      )


    if (token) {

      config.headers =
        config.headers || {}


      config.headers.Authorization =
        'Bearer ' + token

    }


    return config

  },


  error => {

    return Promise.reject(
      error
    )

  }

)


/* ================================ */
/* 响应拦截器 */
/* ================================ */

request.interceptors.response.use(

  response => {

    return response

  },


  error => {

    console.error(
      'API请求失败：',
      error
    )


    /*
     * 请求超时
     */

    if (

      error.code ===
        'ECONNABORTED' ||

      error.message?.includes(
        'timeout'
      )

    ) {

      ElMessage.error(
        '请求超时，请检查后端服务或 AI 服务是否正常'
      )

    }


    /*
     * 后端返回 HTTP 错误
     */

    else if (
      error.response
    ) {

      const status =
        error.response.status


      /*
       * 401
       */

      if (
        status === 401
      ) {

        ElMessage.error(
          '登录已失效，请重新登录'
        )

      }


      /*
       * 403
       */

      else if (
        status === 403
      ) {

        ElMessage.error(
          '没有权限执行此操作'
        )

      }


      /*
       * 500
       */

      else if (
        status >= 500
      ) {

        ElMessage.error(
          '服务器内部错误，请检查后端'
        )

      }


      /*
       * 其他 HTTP 错误
       */

      else {

        const message =
          error.response
            ?.data
            ?.message


        ElMessage.error(

          message ||
          '请求失败'

        )

      }

    }


    /*
     * 后端根本没有启动
     */

    else if (
      error.request
    ) {

      ElMessage.error(
        '无法连接后端服务器，请确认 Spring Boot 已启动'
      )

    }


    return Promise.reject(
      error
    )

  }

)


export default request