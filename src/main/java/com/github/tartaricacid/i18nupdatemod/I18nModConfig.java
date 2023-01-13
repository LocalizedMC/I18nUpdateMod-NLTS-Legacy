package com.github.tartaricacid.i18nupdatemod;

import net.minecraftforge.common.config.Config;

@Config(modid = I18nUpdateMod.MODID, name = I18nUpdateMod.MODID, category = I18nUpdateMod.MODID)
public class I18nModConfig {
    @Config.Name("资源包下载")
    public static Download download = new Download();

    public static class Download{
        @Config.Name("资源包下载源")
        @Config.Comment(
                "CFPA官方下载源：http://downloader1.meitangdehulu.com:22943  \n" +
                "镜像下载源：https://ghproxy.com/https://raw.githubusercontent.com/zkitefly/TranslationPackMirror/main/files  \n" +
                "注意：链接最后不要带任何符号（如：/）"
        )
        public String downloadLink = "https://ghproxy.com/https://raw.githubusercontent.com/zkitefly/TranslationPackMirror/main/files";

        @Config.Name("资源包名称")
        @Config.Comment("用来设置获取下载源的资源包名称（注意：不带文件扩展名！）")
        public String resourcePackName = "Minecraft-Mod-Language-Modpack";

        @Config.Name("MD5名称")
        @Config.Comment("用来设置获取下载源的资源包的MD5名称（注意：不带文件扩展名！）")
        public String md5Name = "1.12.2";
    }
}
