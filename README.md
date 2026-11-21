# AI Study Assistant | AI 智能学习助手

> 从学习目标到每日任务：让 AI 生成的建议变成可执行、可记录、可追踪的学习过程。

基于 **Java 21 + Spring Boot + MyBatis + MySQL + Vue 3 + DeepSeek API** 的前后端分离学习管理系统。用户可以输入学习目标，让 AI 生成计划及每日任务，并通过学习计时、任务打卡、进度统计与 AI 对话完成学习闭环。

**项目定位：AI 应用集成 + Java 全栈业务开发。** 该项目以 AI 规划和学习管理为核心，不属于具备自主工具调用能力的 Agent 系统。

## 功能概览

| 模块 | 已在源码中实现的能力 |
| --- | --- |
| 用户系统 | 注册、登录、个人信息查询与修改；JWT 鉴权、BCrypt 密码哈希 |
| AI 学习辅导 | DeepSeek 对话、最近 10 条聊天记录构建上下文、历史消息持久化 |
| AI 生成计划 | 根据自然语言目标生成结构化 JSON，解析并保存学习计划及对应每日任务 |
| 学习计划 | 新建、编辑、删除、查看计划及目标学时进度 |
| 每日任务 | 按计划查询任务、添加任务、标记完成/取消完成、删除任务 |
| 学习计时与记录 | 前端学习计时、学习记录管理、学习时长累计与分页查询接口 |
| 学习数据看板 | 计划数、学习次数、累计时长、任务完成率及 ECharts 统计图表 |

### 核心体验

1. **输入目标**：例如“希望一个月完成 Java 基础学习”。
2. **AI 制定计划**：后端请求 DeepSeek，要求模型返回包含标题、周期、目标学时和任务列表的 JSON。
3. **落库拆解**：将 AI 输出解析为 Java DTO，保存为 StudyPlan，并逐项生成 StudyTask。
4. **执行学习**：用户在任务列表中打卡，也可以打开计划中的学习计时器。
5. **跟踪进度**：以学习记录累计分钟数计算目标学时完成比例，在首页展示统计图表。

> 注意：AI 计划生成采用提示词约束与 JSON 反序列化；当前尚未使用严格的 JSON Schema 校验，也未实现生成计划与任务写入的数据库事务保护。

## 技术栈

| 层次 | 技术 |
| --- | --- |
| 后端 | Java 21、Spring Boot 3.5.16、Spring MVC、Maven |
| 数据访问 | MyBatis 3.0.5、MySQL、注解式 SQL |
| AI 集成 | DeepSeek Chat Completions API、RestTemplate、Jackson |
| 身份认证 | JWT（jjwt）、BCrypt、Servlet Filter、ThreadLocal |
| 前端 | Vue 3、JavaScript、Vite 8、Vue Router、Element Plus |
| HTTP / 可视化 | Axios、ECharts |
| 其他 | Pinia（已引入项目）、Springdoc OpenAPI / Swagger UI |

## 系统架构

~~~mermaid
flowchart LR
    U["用户"] --> V["Vue 3 + Element Plus"]
    V --> AX["Axios / JWT"]
    AX --> C["Spring Boot Controller"]
    C --> S["业务 Service"]
    S --> M["MyBatis Mapper"]
    M --> DB[("MySQL")]
    S --> AI["DeepSeek API"]
    C --> F["JWT 登录过滤器"]
~~~

### AI 学习计划生成流程

~~~mermaid
flowchart TD
    A["输入学习目标"] --> B["POST /chat/generatePlan"]
    B --> C["构造结构化计划提示词"]
    C --> D["调用 DeepSeek"]
    D --> E["解析 JSON 为 AIPlanResultDTO"]
    E --> F["保存 study_plan"]
    F --> G["逐项保存 study_task"]
    G --> H["前端展示计划与每日任务"]
    H --> I["完成任务 / 记录学时"]
    I --> J["学习进度统计"]
~~~

## 项目结构

~~~text
AI-Study-Assistant/
├── backend/
│   └── ai-study-backend/
│       ├── pom.xml
│       └── src/
│           ├── main/
│           │   ├── java/com/aistudy/backend/
│           │   │   ├── user/       # 用户与账号
│           │   │   ├── chat/       # DeepSeek、AI 对话与计划生成
│           │   │   ├── plan/       # 学习计划
│           │   │   ├── task/       # 学习任务
│           │   │   ├── study/      # 学习记录
│           │   │   ├── common/     # JWT、响应封装、用户上下文
│           │   │   ├── filter/     # 登录过滤器
│           │   │   └── config/     # MVC / Swagger 配置
│           │   └── resources/
│           │       └── application.properties
│           └── test/
├── frontend/
│   ├── src/
│   │   ├── api/           # Axios 请求封装
│   │   ├── components/    # StudyTimer 学习计时器
│   │   ├── layout/        # 应用布局
│   │   ├── router/        # Vue Router
│   │   └── views/         # 登录、首页、计划、任务、记录、AI 对话
│   ├── package.json
│   └── vite.config.js
└── README.md
~~~

## API 接口（共 21 个）

所有路径均相对于后端地址。除注册和登录外，业务接口需要携带 Authorization: Bearer TOKEN。

| 模块 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 用户 | POST | /user/register | 用户注册 |
| 用户 | POST | /user/login | 用户登录 |
| 用户 | GET | /user/info | 当前用户信息 |
| 用户 | PUT | /user/profile | 更新个人信息 |
| 用户 | POST | /user/logout | 退出接口（客户端清理 Token） |
| AI | POST | /chat | AI 问答 |
| AI | GET | /chat/history | 最近聊天记录 |
| AI | POST | /chat/generatePlan | AI 生成并保存学习计划 |
| 计划 | POST | /plan/add | 添加学习计划 |
| 计划 | GET | /plan/list | 查询计划与学习进度 |
| 计划 | PUT | /plan/update | 修改学习计划 |
| 计划 | DELETE | /plan/{id} | 删除学习计划 |
| 任务 | POST | /task/add | 添加任务 |
| 任务 | GET | /task/list/{planId} | 按计划查询任务 |
| 任务 | PUT | /task/status | 更新完成状态 |
| 任务 | DELETE | /task/{id} | 删除任务 |
| 学习记录 | POST | /study/add | 添加学习记录 |
| 学习记录 | GET | /study/list | 查询学习记录 |
| 学习记录 | PUT | /study/update | 修改学习记录 |
| 学习记录 | DELETE | /study/{id} | 删除学习记录 |
| 学习记录 | GET | /study/page | 分页查询学习记录 |

后端提供 Swagger UI（默认地址：<http://localhost:8080/swagger-ui/index.html>），可在本地启动后查看请求结构。

## 数据模型

源码中的 MyBatis Mapper 访问以下 **5 张业务表**：

| 表名 | 用途 | 主要关系 |
| --- | --- | --- |
| user | 用户账号 | 用户 ID |
| chat_record | 问答历史 | user_id |
| study_plan | 计划信息、起止日期与目标学时 | user_id |
| study_task | 每日任务与完成状态 | user_id、plan_id |
| study_record | 学习内容与学习时长（分钟） | user_id、plan_id |

数据库字段与 SQL 以实体及 Mapper 源码为准。

## 本地启动

### 环境要求

- JDK 21、Maven（或项目 Maven Wrapper）
- MySQL（使用与 MyBatis Mapper 兼容的表结构）
- Node.js 22.12+ 与 npm（推荐用于当前 Vite 8 依赖）
- 可用的 DeepSeek API Key

**重要：当前仓库未包含数据库建表脚本及 application-local.properties。仅执行以下命令并不能在全新环境中直接运行全部业务。** 请先按照 Mapper / Entity 准备数据表，并配置自己的数据库和 API Key；不要将真实密码提交到 Git。

### 1. 准备本地配置

源码中 application.properties 已激活 local Profile。在以下位置新建被 Git 忽略的配置文件：

backend/ai-study-backend/src/main/resources/application-local.properties

参考配置（仅为本地示例，需按实际数据库调整）：

~~~properties
spring.datasource.url=jdbc:mysql://localhost:3306/ai_study?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
deepseek.api-key=YOUR_DEEPSEEK_API_KEY
~~~

### 2. 启动后端

~~~bash
cd backend/ai-study-backend
./mvnw spring-boot:run
~~~

Windows 可使用 mvnw.cmd。默认后端地址为 <http://localhost:8080>。

### 3. 启动前端

在另一个终端：

~~~bash
cd frontend
npm ci
npm run dev
~~~

默认访问 <http://localhost:5173>。当前 Axios 的 baseURL 固定为 http://localhost:8080，后端 CORS 也针对 http://localhost:5173；跨机器运行需要调整相关配置。

### 4. 验证主要流程

注册 / 登录 → AI 对话 → 生成学习计划 → 查看计划和任务 → 完成任务 / 添加学习记录 → 首页查看统计。

## 已实现的工程设计

- **业务分层**：Controller、Service、Mapper、DTO / Entity / VO 分离。
- **AI 结果结构化**：让 DeepSeek 返回约定 JSON，经 Jackson 解析后写入计划及任务表。
- **用户数据隔离**：核心查询和写入条件包含登录用户 ID；身份信息由 JWT Filter 解析并使用 ThreadLocal 传递，请求结束清理。
- **学习进度计算**：累计学习分钟数与目标小时数换算，并在接口与 ECharts 看板中展示。
- **统一接口形态**：统一 Result 响应、业务异常处理、部分 DTO 参数校验和 Swagger 文档。

## 当前限制与改进计划

以下是基于当前公开源码的真实工程状态，不代表已完成的功能：

- JWT 签名密钥目前硬编码在 JwtUtil.java 中；**公开部署前必须改为环境变量并轮换密钥**。
- AI 计划与任务落库尚无事务保护；建议增加校验、事务和 AI 返回异常重试。
- 前端手动新增学习记录尚未传入后端要求的 planId；需要统一表单字段与接口校验。
- 仓库缺少 schema.sql / 数据迁移脚本、可复现的 Docker 配置和完整的自动化测试。当前仅包含 Spring Boot 上下文加载测试。
- 可继续优化 Token 失效处理、任务归属校验、配置外置化以及移动端适配。

> **使用声明**：当前版本适合作为 Java / AI 应用开发的学习与作品展示项目。安全性、可复现部署和测试覆盖仍需完善，不建议未经加固直接开放公网服务。

## 项目演示

建议在后续提交中补充真实运行截图：登录页、AI 生成计划、任务列表、学习计时器以及 ECharts 首页看板。当前仓库尚未提供这些功能的正式演示截图，故不使用占位图片冒充效果图。

---

**AI Study Assistant**：用 AI 生成学习路径，用系统化的任务、计时和数据反馈推动学习目标落地。
