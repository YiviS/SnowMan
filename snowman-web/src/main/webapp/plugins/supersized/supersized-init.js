jQuery(function($){

    $.supersized({

        // Functionality
        slide_interval     : 4000,    // 图片切换间隔时间（毫秒）
        transition         : 1,    // 图片切换效果，0-无，1-淡入淡出，2-向上滑动，3-向右滑动，4-向下滑动，5-向左滑动。
        transition_speed   : 1000,    // 切换速度，默认750。
        performance        : 1,    //  0 - 正常，1 - 混合速度/质量，2 - 更优的图像质量，三优的转换速度//（仅适用于火狐/ IE浏览器，而不是Webkit的）

        // Size & Position
        min_width          : 0,    // 最小允许宽度（以像素为单位）
        min_height         : 0,    // 最小允许高度（以像素为单位）
        vertical_center    : 1,    // 是否让图像垂直居中，如果为0，则图像为顶端对齐。
        horizontal_center  : 1,    // 水平中心的背景
        fit_always         : 0,    // 图像绝不会超过浏览器的宽度或高度（忽略分钟。尺寸）
        fit_portrait       : 1,    // 纵向图像将不超过浏览器高度
        fit_landscape      : 0,    // 景观的图像将不超过宽度的浏览器

        // Components
        slide_links        : 'blank',    // 个别环节为每张幻灯片（选项：假的，'民'，'名'，'空'）
        slides             : [    // //所切换的图片集合，包括图片地址image，图片标题title，图片链接url。
                                 {image : '../static/images/backgrounds/1.jpg'},
                                 {image : '../static/images/backgrounds/2.jpg'},
                                 {image : '../static/images/backgrounds/3.jpg'}
                             ]

    });

});
