/*
 * MIT License
 *
 * Copyright (c) 2018 Aberic Yang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.aberic.bother.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 作者：Aberic on 2018/09/20 12:27
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Component
@Slf4j
public class ScheduledTasks {

    /**
     * fixedDelay = x 表示当前方法执行完毕x ms后，Spring scheduling会再次调用该方法<p>
     * 选举定时检测方案<p>
     * 每一次执行就会检查当前出块节点是否到出块时间<p>
     * 如果到出块时间，则发送给竞选节点集合中的协助节点进行催促
     */
    @Scheduled(fixedDelay = 5000)
    public void electionCheck() {
        log.info("===============>>>>>>>>>> fixedDelay = 5000 <<<<<<<<<<===============");
    }

}
