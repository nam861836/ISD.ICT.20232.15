//package subsystem.vnPay;
//
//import common.exception.UnrecognizedException;
//import utils.ApplicationProgrammingInterface;
//
//public class VnPayService {
//
//
//  /**
//   * @param url
//   * @param data
//   * @return String
//   */
//  String query(String url, String data) {
//    String response = null;
//    try {
//      response = ApplicationProgrammingInterface.get(url + "?" +  data, null);
//    } catch (Exception e) {
//      throw new UnrecognizedException();
//    }
//    return response;
//  }
//
//}
