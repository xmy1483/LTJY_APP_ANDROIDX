package com.bac.commonlib.http.cookie.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 将cookie保存到内存
 */
public class MemoryCookieStore implements CookieStore
{
    // 保存cookie的集合
    private final HashMap<String, List<Cookie>> allCookies = new HashMap<>();

    /**
     * 添加
     * @param url
     * @param cookies
     */
    @Override
    public void add(HttpUrl url, List<Cookie> cookies)
    {
        // 以主机头为key
        List<Cookie> oldCookies = allCookies.get(url.host());

        if (oldCookies != null)
        {
            // 已经存在
            Iterator<Cookie> itNew = cookies.iterator(); // 新
            Iterator<Cookie> itOld = oldCookies.iterator(); // 旧
            while (itNew.hasNext())
            {
                String va = itNew.next().name();
                while (va != null && itOld.hasNext())
                {
                    String v = itOld.next().name();
                    if (v != null && va.equals(v))
                    {
                        // 相同
                        itOld.remove();// 从旧集合移除
                    }
                }
            }
            oldCookies.addAll(cookies);// 将新数据添加至就集合
        } else
        {
            allCookies.put(url.host(), cookies);// 添加cookie
        }
    }

    /**
     * 获取
     * @param uri
     * @return
     */
    @Override
    public List<Cookie> get(HttpUrl uri)
    {
        List<Cookie> cookies = allCookies.get(uri.host());// 根据主机头获取已经存在的集合
        if (cookies == null)
        {
            cookies = new ArrayList<>();
            allCookies.put(uri.host(), cookies);
        }
        return cookies;

    }

    /**
     * 清空保存cookie的集合
     * @return
     */
    @Override
    public boolean removeAll()
    {
        allCookies.clear();
        return true;
    }

    /**
     * 获取所有的cookie
     * @return
     */
    @Override
    public List<Cookie> getCookies()
    {
        List<Cookie> cookies = new ArrayList<>();
        Set<String> httpUrls = allCookies.keySet();
        for (String url : httpUrls)
        {
            cookies.addAll(allCookies.get(url));
        }
        return cookies;
    }


    /**
     * 移除指定的cookie
     * @param uri
     * @param cookie
     * @return
     */
    @Override
    public boolean remove(HttpUrl uri, Cookie cookie)
    {
        List<Cookie> cookies = allCookies.get(uri.host());
        if (cookie != null)
        {
            return cookies.remove(cookie);
        }
        return false;
    }


}
