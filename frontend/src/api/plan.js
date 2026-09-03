import request from './request'


// 添加计划
export function addPlan(data){

    return request.post(
        '/plan/add',
        data
    )

}


// 查询计划
export function getPlanList(){

    return request.get(
        '/plan/list'
    )

}


// 修改计划
export function updatePlan(data){

    return request.put(
        '/plan/update',
        data
    )

}


// 删除计划
export function deletePlan(id){

    return request.delete(
        `/plan/${id}`
    )

}