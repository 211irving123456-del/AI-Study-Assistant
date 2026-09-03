import request from './request'



// 查询任务
export function getTaskList(planId){

    return request.get(
        `/task/list/${planId}`
    )

}



// 新增任务
export function addTask(data){

    return request.post(
        '/task/add',
        data
    )

}



// 修改状态
export function updateStatus(data){

    return request.put(
        '/task/status',
        data
    )

}



// 删除
export function deleteTask(id){

    return request.delete(
        `/task/${id}`
    )

}