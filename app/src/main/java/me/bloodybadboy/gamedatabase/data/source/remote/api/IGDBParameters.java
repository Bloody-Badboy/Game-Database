package me.bloodybadboy.gamedatabase.data.source.remote.api;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IGDBParameters {
  private final Map<String, String> queryMap = new HashMap<>();
  private final List<String> fields = new ArrayList<>();
  private final List<String> expands = new ArrayList<>();

  public Map<String, String> getQueryMap() {
    if (fields.size() > 0) {
      queryMap.put("fields", TextUtils.join(",", fields));
    }
    if (expands.size() > 0) {
      queryMap.put("expand", TextUtils.join(",", expands));
    }
    return queryMap;
  }

  public IGDBParameters addField(String field) {
    fields.add(field.replaceAll(" ", ""));
    return this;
  }

  public IGDBParameters addFilter(String name, String value) {
    queryMap.put("filter" + name.replaceAll(" ", ""), value.replaceAll(" ", ""));
    return this;
  }

  public IGDBParameters addOffset(String offset) {
    queryMap.put("offset", offset.replaceAll(" ", ""));
    return this;
  }

  public IGDBParameters addLimit(int limit) {
    queryMap.put("limit", String.valueOf(limit));
    return this;
  }

  public IGDBParameters addOrder(String order) {
    queryMap.put("order", order.replaceAll(" ", ""));
    return this;
  }

  public IGDBParameters addSearch(String search) {
    queryMap.put("search", search.replaceAll(" ", "%20"));
    return this;
  }

  public IGDBParameters addExpand(String expand) {
    expands.add(expand.replaceAll(" ", ""));
    return this;
  }
}
