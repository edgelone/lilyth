package com.tujia.os.lilyth.controller;

import com.tujia.os.lilyth.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lk
 * @date 2019/4/4
 */
@RestController
public class MainController {
  @Autowired
  private AnalysisService analysisService;

  @GetMapping("/main")
  public String main() {
    analysisService.analysisAvatarFile();
    analysisService.analysisContract();
    analysisService.analysisGrootFile();
    analysisService.analysisRetailer();
    analysisService.analysisRateUser();
    analysisService.analysisRate();
    return "success";
  }
}
