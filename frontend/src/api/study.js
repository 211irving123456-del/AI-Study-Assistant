import request from './request'


// 查询学习记录
export function getStudyList(){

    return request.get(
        '/study/list'
    )

}



// 添加学习记录
export function addStudy(data){

    return request.post(
        '/study/add',
        data
    )

}



// 新名字（给StudyTimer使用）
export function addStudyRecord(data){

    return request.post(
        '/study/add',
        data
    )

}



// 修改学习记录
export function updateStudy(data){

    return request.put(
        '/study/update',
        data
    )

}



// 删除学习记录
export function deleteStudy(id){

    return request.delete(
        `/study/${id}`
    )

}