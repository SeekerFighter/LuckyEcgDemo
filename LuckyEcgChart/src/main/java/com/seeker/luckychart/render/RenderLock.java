package com.seeker.luckychart.render;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Seeker
 * @date 2018/10/19/019  16:28
 */
public class RenderLock {

    public static final ReentrantLock LOCK = new ReentrantLock(true);

}
