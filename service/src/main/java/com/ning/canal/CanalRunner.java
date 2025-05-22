package com.ning.canal;

import org.springframework.boot.CommandLineRunner;

// 需要项目启动就运行一些东西的时候 就继承这个类
//@Component
public class CanalRunner implements CommandLineRunner {

//    @Autowired
    private CanalService canalService;

    @Override
    public void run(String... args) throws Exception {
        // 启动 Canal 监听
//        canalService.listen();
    }
}
