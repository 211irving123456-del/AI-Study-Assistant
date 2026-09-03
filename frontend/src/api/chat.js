import request from './request'


// AI聊天
export function chat(data){

    return request.post(
        '/chat',
        data
    )

}



// 查询聊天历史
export function getHistory(){

    return request.get(
        '/chat/history'
    )

}



// AI生成学习计划
export function generatePlan(data){

    return request.post(
        '/chat/generatePlan',
        data
    )

}