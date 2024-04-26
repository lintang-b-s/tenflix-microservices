package com.lintang.netflik.notificationservice.service;

import io.rocketbase.mail.EmailTemplateBuilder;
import com.lintang.netflik.notificationservice.dto.MovieEvent;
import com.lintang.netflik.notificationservice.email.EmailSenderServiceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class EmailConsumerService {

  private final EmailSenderServiceImpl emailService;
  EmailTemplateBuilder.EmailTemplateConfigBuilder builder;


  public EmailConsumerService( EmailSenderServiceImpl emailService){
    this.emailService = emailService;
  }


  @Value("${email.to}")
  private String emailDestination;


  public void emailNotifNewMovie(MovieEvent message) {
    String messageContentBody = "film" +  message.getName() + "telah tersedia di tenflix. " + message.getSynopsis();

    emailService.send(emailDestination, message.getName(),
      buildEmail(message.getName(), messageContentBody,
      message.getImage()));
    return ;
  }

   public String buildEmail(String movieTitle, String mContentBody,
   String imageUrl) {
     return "<!DOCTYPE html>\n" +
                     "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                     "<head>\n" +
                     "    \n" +
                     "\n" +
                     "    <link href=\"https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700\" rel=\"stylesheet\">\n" +
                     "\n" +
                     "    <!-- CSS Reset : BEGIN -->\n" +
                     "<style>\n" +
                     "html,\n" +
                     "body {\n" +
                     "    margin: 0 auto !important;\n" +
                     "    padding: 0 !important;\n" +
                     "    height: 100% !important;\n" +
                     "    width: 100% !important;\n" +
                     "    background: #f1f1f1;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: Stops email clients resizing small text. */\n" +
                     "* {\n" +
                     "    -ms-text-size-adjust: 100%;\n" +
                     "    -webkit-text-size-adjust: 100%;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: Centers email on Android 4.4 */\n" +
                     "div[style*=\"margin: 16px 0\"] {\n" +
                     "    margin: 0 !important;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: Stops Outlook from adding extra spacing to tables. */\n" +
                     "table,\n" +
                     "td {\n" +
                     "    mso-table-lspace: 0pt !important;\n" +
                     "    mso-table-rspace: 0pt !important;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: Fixes webkit padding issue. */\n" +
                     "table {\n" +
                     "    border-spacing: 0 !important;\n" +
                     "    border-collapse: collapse !important;\n" +
                     "    table-layout: fixed !important;\n" +
                     "    margin: 0 auto !important;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: Uses a better rendering method when resizing images in IE. */\n" +
                     "img {\n" +
                     "    -ms-interpolation-mode:bicubic;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: Prevents Windows 10 Mail from underlining links despite inline CSS. Styles for underlined links should be inline. */\n" +
                     "a {\n" +
                     "    text-decoration: none;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: A work-around for email clients meddling in triggered links. */\n" +
                     "*[x-apple-data-detectors],  /* iOS */\n" +
                     ".unstyle-auto-detected-links *,\n" +
                     ".aBn {\n" +
                     "    border-bottom: 0 !important;\n" +
                     "    cursor: default !important;\n" +
                     "    color: inherit !important;\n" +
                     "    text-decoration: none !important;\n" +
                     "    font-size: inherit !important;\n" +
                     "    font-family: inherit !important;\n" +
                     "    font-weight: inherit !important;\n" +
                     "    line-height: inherit !important;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: Prevents Gmail from displaying a download button on large, non-linked images. */\n" +
                     ".a6S {\n" +
                     "    display: none !important;\n" +
                     "    opacity: 0.01 !important;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: Prevents Gmail from changing the text color in conversation threads. */\n" +
                     ".im {\n" +
                     "    color: inherit !important;\n" +
                     "}\n" +
                     "\n" +
                     "/* If the above doesn't work, add a .g-img class to any image in question. */\n" +
                     "img.g-img + div {\n" +
                     "    display: none !important;\n" +
                     "}\n" +
                     "\n" +
                     "/* What it does: Removes right gutter in Gmail iOS app: https://github.com/TedGoas/Cerberus/issues/89  */\n" +
                     "/* Create one of these media queries for each additional viewport size you'd like to fix */\n" +
                     "\n" +
                     "/* iPhone 4, 4S, 5, 5S, 5C, and 5SE */\n" +
                     "@media only screen and (min-device-width: 320px) and (max-device-width: 374px) {\n" +
                     "    u ~ div .email-container {\n" +
                     "        min-width: 320px !important;\n" +
                     "    }\n" +
                     "}\n" +
                     "/* iPhone 6, 6S, 7, 8, and X */\n" +
                     "@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {\n" +
                     "    u ~ div .email-container {\n" +
                     "        min-width: 375px !important;\n" +
                     "    }\n" +
                     "}\n" +
                     "/* iPhone 6+, 7+, and 8+ */\n" +
                     "@media only screen and (min-device-width: 414px) {\n" +
                     "    u ~ div .email-container {\n" +
                     "        min-width: 414px !important;\n" +
                     "    }\n" +
                     "}\n" +
                     "\n" +
                     "</style>\n" +
                     "\n" +
                     "<!-- CSS Reset : END -->\n" +
                     "\n" +
                     "<!-- Progressive Enhancements : BEGIN -->\n" +
                     "<style>\n" +
                     "\n" +
                     "  .primary{\n" +
                     "\tbackground: #0d0cb5;\n" +
                     "}\n" +
                     ".bg_white{\n" +
                     "\tbackground: #ffffff;\n" +
                     "}\n" +
                     ".bg_light{\n" +
                     "\tbackground: #fafafa;\n" +
                     "}\n" +
                     ".bg_black{\n" +
                     "\tbackground: #000000;\n" +
                     "}\n" +
                     ".bg_dark{\n" +
                     "\tbackground: rgba(0,0,0,.8);\n" +
                     "}\n" +
                     ".email-section{\n" +
                     "\tpadding:2.5em;\n" +
                     "}\n" +
                     "\n" +
                     "/*BUTTON*/\n" +
                     ".btn{\n" +
                     "\tpadding: 5px 15px;\n" +
                     "\tdisplay: inline-block;\n" +
                     "}\n" +
                     ".btn.btn-primary{\n" +
                     "\tborder-radius: 5px;\n" +
                     "\tbackground: #0d0cb5;\n" +
                     "\tcolor: #ffffff;\n" +
                     "}\n" +
                     ".btn.btn-white{\n" +
                     "\tborder-radius: 5px;\n" +
                     "\tbackground: #ffffff;\n" +
                     "\tcolor: #000000;\n" +
                     "}\n" +
                     ".btn.btn-white-outline{\n" +
                     "\tborder-radius: 5px;\n" +
                     "\tbackground: transparent;\n" +
                     "\tborder: 1px solid #fff;\n" +
                     "\tcolor: #fff;\n" +
                     "}\n" +
                     "\n" +
                     "h1,h2,h3,h4,h5,h6{\n" +
                     "\tfont-family: 'Poppins', sans-serif;\n" +
                     "\tcolor: #000000;\n" +
                     "\tmargin-top: 0;\n" +
                     "}\n" +
                     "\n" +
                     "body{\n" +
                     "\tfont-family: 'Poppins', sans-serif;\n" +
                     "\tfont-weight: 400;\n" +
                     "\tfont-size: 15px;\n" +
                     "\tline-height: 1.8;\n" +
                     "\tcolor: rgba(0,0,0,.4);\n" +
                     "}\n" +
                     "\n" +
                     "a{\n" +
                     "\tcolor: #0d0cb5;\n" +
                     "}\n" +
                     "\n" +
                     "table{\n" +
                     "}\n" +
                     "/*LOGO*/\n" +
                     "\n" +
                     ".logo h1{\n" +
                     "\tmargin: 0;\n" +
                     "}\n" +
                     ".logo h1 a{\n" +
                     "\tcolor: #000000;\n" +
                     "\tfont-size: 20px;\n" +
                     "\tfont-weight: 700;\n" +
                     "\ttext-transform: uppercase;\n" +
                     "\tfont-family: 'Poppins', sans-serif;\n" +
                     "}\n" +
                     "\n" +
                     ".navigation{\n" +
                     "\tpadding: 0;\n" +
                     "}\n" +
                     ".navigation li{\n" +
                     "\tlist-style: none;\n" +
                     "\tdisplay: inline-block;;\n" +
                     "\tmargin-left: 5px;\n" +
                     "\tfont-size: 13px;\n" +
                     "\tfont-weight: 500;\n" +
                     "}\n" +
                     ".navigation li a{\n" +
                     "\tcolor: rgba(0,0,0,.4);\n" +
                     "}\n" +
                     "\n" +
                     "/*HERO*/\n" +
                     ".hero{\n" +
                     "\tposition: relative;\n" +
                     "\tz-index: 0;\n" +
                     "}\n" +
                     ".hero .overlay{\n" +
                     "\tposition: absolute;\n" +
                     "\ttop: 0;\n" +
                     "\tleft: 0;\n" +
                     "\tright: 0;\n" +
                     "\tbottom: 0;\n" +
                     "\tcontent: '';\n" +
                     "\twidth: 100%;\n" +
                     "\tbackground: #000000;\n" +
                     "\tz-index: -1;\n" +
                     "\topacity: .3;\n" +
                     "}\n" +
                     ".hero .icon{\n" +
                     "}\n" +
                     ".hero .icon a{\n" +
                     "\tdisplay: block;\n" +
                     "\twidth: 60px;\n" +
                     "\tmargin: 0 auto;\n" +
                     "}\n" +
                     ".hero .text{\n" +
                     "\tcolor: rgba(255,255,255,.8);\n" +
                     "}\n" +
                     ".hero .text h2{\n" +
                     "\tcolor: #ffffff;\n" +
                     "\tfont-size: 30px;\n" +
                     "\tmargin-bottom: 0;\n" +
                     "}\n" +
                     "\n" +
                     "\n" +
                     "/*HEADING SECTION*/\n" +
                     ".heading-section{\n" +
                     "}\n" +
                     ".heading-section h2{\n" +
                     "\tcolor: #000000;\n" +
                     "\tfont-size: 20px;\n" +
                     "\tmargin-top: 0;\n" +
                     "\tline-height: 1.4;\n" +
                     "\tfont-weight: 700;\n" +
                     "\ttext-transform: uppercase;\n" +
                     "}\n" +
                     ".heading-section .subheading{\n" +
                     "\tmargin-bottom: 20px !important;\n" +
                     "\tdisplay: inline-block;\n" +
                     "\tfont-size: 13px;\n" +
                     "\ttext-transform: uppercase;\n" +
                     "\tletter-spacing: 2px;\n" +
                     "\tcolor: rgba(0,0,0,.4);\n" +
                     "\tposition: relative;\n" +
                     "}\n" +
                     ".heading-section .subheading::after{\n" +
                     "\tposition: absolute;\n" +
                     "\tleft: 0;\n" +
                     "\tright: 0;\n" +
                     "\tbottom: -10px;\n" +
                     "\tcontent: '';\n" +
                     "\twidth: 100%;\n" +
                     "\theight: 2px;\n" +
                     "\tbackground: #0d0cb5;\n" +
                     "\tmargin: 0 auto;\n" +
                     "}\n" +
                     "\n" +
                     ".heading-section-white{\n" +
                     "\tcolor: rgba(255,255,255,.8);\n" +
                     "}\n" +
                     ".heading-section-white h2{\n" +
                     "\tfont-family: \n" +
                     "\tline-height: 1;\n" +
                     "\tpadding-bottom: 0;\n" +
                     "}\n" +
                     ".heading-section-white h2{\n" +
                     "\tcolor: #ffffff;\n" +
                     "}\n" +
                     ".heading-section-white .subheading{\n" +
                     "\tmargin-bottom: 0;\n" +
                     "\tdisplay: inline-block;\n" +
                     "\tfont-size: 13px;\n" +
                     "\ttext-transform: uppercase;\n" +
                     "\tletter-spacing: 2px;\n" +
                     "\tcolor: rgba(255,255,255,.4);\n" +
                     "}\n" +
                     "\n" +
                     "\n" +
                     ".icon{\n" +
                     "\ttext-align: center;\n" +
                     "}\n" +
                     ".icon img{\n" +
                     "}\n" +
                     "\n" +
                     "\n" +
                     "/*SERVICES*/\n" +
                     ".services{\n" +
                     "\tbackground: rgba(0,0,0,.03);\n" +
                     "}\n" +
                     ".text-services{\n" +
                     "\tpadding: 10px 10px 0; \n" +
                     "\ttext-align: center;\n" +
                     "}\n" +
                     ".text-services h3{\n" +
                     "\tfont-size: 16px;\n" +
                     "\tfont-weight: 600;\n" +
                     "}\n" +
                     "\n" +
                     ".services-list{\n" +
                     "\tpadding: 0;\n" +
                     "\tmargin: 0 0 20px 0;\n" +
                     "\twidth: 100%;\n" +
                     "\tfloat: left;\n" +
                     "}\n" +
                     "\n" +
                     ".services-list img{\n" +
                     "\tfloat: left;\n" +
                     "}\n" +
                     ".services-list .text{\n" +
                     "\twidth: calc(100% - 60px);\n" +
                     "\tfloat: right;\n" +
                     "}\n" +
                     ".services-list h3{\n" +
                     "\tmargin-top: 0;\n" +
                     "\tmargin-bottom: 0;\n" +
                     "}\n" +
                     ".services-list p{\n" +
                     "\tmargin: 0;\n" +
                     "}\n" +
                     "\n" +
                     "/*BLOG*/\n" +
                     ".text-services .meta{\n" +
                     "\ttext-transform: uppercase;\n" +
                     "\tfont-size: 14px;\n" +
                     "}\n" +
                     "\n" +
                     "/*TESTIMONY*/\n" +
                     ".text-testimony .name{\n" +
                     "\tmargin: 0;\n" +
                     "}\n" +
                     ".text-testimony .position{\n" +
                     "\tcolor: rgba(0,0,0,.3);\n" +
                     "\n" +
                     "}\n" +
                     "\n" +
                     "\n" +
                     "/*VIDEO*/\n" +
                     ".img{\n" +
                     "\twidth: 100%;\n" +
                     "\theight: auto;\n" +
                     "\tposition: relative;\n" +
                     "}\n" +
                     ".img .icon{\n" +
                     "\tposition: absolute;\n" +
                     "\ttop: 50%;\n" +
                     "\tleft: 0;\n" +
                     "\tright: 0;\n" +
                     "\tbottom: 0;\n" +
                     "\tmargin-top: -25px;\n" +
                     "}\n" +
                     ".img .icon a{\n" +
                     "\tdisplay: block;\n" +
                     "\twidth: 60px;\n" +
                     "\tposition: absolute;\n" +
                     "\ttop: 0;\n" +
                     "\tleft: 50%;\n" +
                     "\tmargin-left: -25px;\n" +
                     "}\n" +
                     "\n" +
                     "\n" +
                     "\n" +
                     "/*COUNTER*/\n" +
                     ".counter{\n" +
                     "\twidth: 100%;\n" +
                     "\tposition: relative;\n" +
                     "\tz-index: 0;\n" +
                     "}\n" +
                     ".counter .overlay{\n" +
                     "\tposition: absolute;\n" +
                     "\ttop: 0;\n" +
                     "\tleft: 0;\n" +
                     "\tright: 0;\n" +
                     "\tbottom: 0;\n" +
                     "\tcontent: '';\n" +
                     "\twidth: 100%;\n" +
                     "\tbackground: #000000;\n" +
                     "\tz-index: -1;\n" +
                     "\topacity: .3;\n" +
                     "}\n" +
                     ".counter-text{\n" +
                     "\ttext-align: center;\n" +
                     "}\n" +
                     ".counter-text .num{\n" +
                     "\tdisplay: block;\n" +
                     "\tcolor: #ffffff;\n" +
                     "\tfont-size: 34px;\n" +
                     "\tfont-weight: 700;\n" +
                     "}\n" +
                     ".counter-text .name{\n" +
                     "\tdisplay: block;\n" +
                     "\tcolor: rgba(255,255,255,.9);\n" +
                     "\tfont-size: 13px;\n" +
                     "}\n" +
                     "\n" +
                     "\n" +
                     "/*FOOTER*/\n" +
                     "\n" +
                     ".footer{\n" +
                     "\tcolor: rgba(255,255,255,.5);\n" +
                     "\n" +
                     "}\n" +
                     ".footer .heading{\n" +
                     "\tcolor: #ffffff;\n" +
                     "\tfont-size: 20px;\n" +
                     "}\n" +
                     ".footer ul{\n" +
                     "\tmargin: 0;\n" +
                     "\tpadding: 0;\n" +
                     "}\n" +
                     ".footer ul li{\n" +
                     "\tlist-style: none;\n" +
                     "\tmargin-bottom: 10px;\n" +
                     "}\n" +
                     ".footer ul li a{\n" +
                     "\tcolor: rgba(255,255,255,1);\n" +
                     "}\n" +
                     "\n" +
                     "\n" +
                     "@media screen and (max-width: 500px) {\n" +
                     "\n" +
                     "\t.icon{\n" +
                     "\t\ttext-align: left;\n" +
                     "\t}\n" +
                     "\n" +
                     "\t.text-services{\n" +
                     "\t\tpadding-left: 0;\n" +
                     "\t\tpadding-right: 20px;\n" +
                     "\t\ttext-align: left;\n" +
                     "\t}\n" +
                     "\n" +
                     "}\n" +
                     "</style>\n" +
                     "\n" +
                     "\n" +
                     "</head>\n" +
                     "\n" +
                     "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #222222;\">\n" +
                     "\t<center style=\"width: 100%; background-color: #f1f1f1;\">\n" +
                     "    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n" +
                     "      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;\n" +
                     "    </div>\n" +
                     "    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">\n" +
                     "    \t<!-- BEGIN BODY -->\n" +
                     "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                     "      \n" +
                     "\t\t\t\t<tr>\n" +
                     "          <td valign=\"middle\" class=\"hero bg_white\" style=\"background-image: url(images/"+imageUrl+"); background-size: cover; height: 400px;\">\n" +
                     "          \t<div class=\"overlay\"></div>\n" +
                     "            <table>\n" +
                     "            \t<tr>\n" +
                     "            \t\t<td>\n" +
                     "            \t\t\t<div class=\"text\" style=\"padding: 0 3em; text-align: center;\">\n" +
                     "            \t\t\t\t<h2>"+ movieTitle +" telah rilis di netflik</h2>\n" +
                     "            \t\t\t\t<p>"+mContentBody+"</p>\n" +
                     "            \t\t\t\t<div class=\"icon\">\n" +
                     "\t            \t\t\t\t<a href=\"#\">\n" +
                     "\t                      <img src=\"images/002-play-button.png\" alt=\"\" style=\"width: 60px; max-width: 600px; height: auto; margin: auto; display: block;\">\n" +
                     "\t                    </a>\n" +
                     "\t                  </div>\n" +
                     "            \t\t\t</div>\n" +
                     "            \t\t</td>\n" +
                     "            \t</tr>\n" +
                     "            </table>\n" +
                     "          </td>\n" +
                     "\t      </tr><!-- end tr -->\n" +
                     "\t      <tr>\n" +
                     "\t\t      <td class=\"bg_white\">\n" +
                     "\t\t        <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
                     "\t\t          <tr>\n" +
                     "\t\t            <td class=\"bg_white email-section\">\n" +
                     "\t\t            \t<div class=\"heading-section\" style=\"text-align: center; padding: 0 30px;\">\n" +
                     "\t\t              \t<h2>Saksikan "+movieTitle+" di netflik sekarang juga</h2>\n" +
                     "\t\t\t\t\t\t\t\t\t\t<img src=\"https://drive.google.com/uc?export=view&id=1zJD6ZhmAr0KBpA_aoVZn5VCpLAv_Ho3i\" alt=\"\" style=\"width: 720px; max-width: 720px; height: auto; margin: auto; display: block;\">\n" +
                     "\n" +
                     "\t\t            \t</div>\n" +
                     "\t\t\t\t\t\t\t\t\n" +
                     "\t\t            </td>\n" +
                     "\t\t          </tr><!-- end: tr -->\n" +
                     "\t\t\t\t\t\t\t\n" +
                     "\t\t        </table>\n" +
                     "\n" +
                     "\t\t      </td>\n" +
                     "\t\t    </tr><!-- end:tr -->\n" +
                     "      <!-- 1 Column Text + Button : END -->\n" +
                     "      </table>\n" +
                     "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                     "      \t<tr>\n" +
                     "          <td valign=\"middle\" class=\"bg_black footer email-section\">\n" +
                     "            <table>\n" +
                     "            \t<tr>\n" +
                     "                <td valign=\"top\" width=\"33.333%\" style=\"padding-top: 20px;\">\n" +
                     "                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
                     "                    <tr>\n" +
                     "                      <td style=\"text-align: left; padding-right: 10px;\">\n" +
                     "                      \t<h3 class=\"heading\">About</h3>\n" +
                     "                      \t<p>A small river named Duden flows by their place and supplies it with the necessary regelialia.</p>\n" +
                     "                      </td>\n" +
                     "                    </tr>\n" +
                     "                  </table>\n" +
                     "                </td>\n" +
                     "                <td valign=\"top\" width=\"33.333%\" style=\"padding-top: 20px;\">\n" +
                     "                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
                     "                    <tr>\n" +
                     "                      <td style=\"text-align: left; padding-left: 5px; padding-right: 5px;\">\n" +
                     "                      \t<h3 class=\"heading\">Contact Info</h3>\n" +
                     "                      \t<ul>\n" +
                     "\t\t\t\t\t                <li><span class=\"text\">203 Fake St. Mountain View, San Francisco, California, USA</span></li>\n" +
                     "\t\t\t\t\t                <li><span class=\"text\">+2 392 3929 210</span></a></li>\n" +
                     "\t\t\t\t\t              </ul>\n" +
                     "                      </td>\n" +
                     "                    </tr>\n" +
                     "                  </table>\n" +
                     "                </td>\n" +
                     "                <td valign=\"top\" width=\"33.333%\" style=\"padding-top: 20px;\">\n" +
                     "                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
                     "                    <tr>\n" +
                     "                      <td style=\"text-align: left; padding-left: 10px;\">\n" +
                     "                      \t<h3 class=\"heading\">Useful Links</h3>\n" +
                     "                      \t<ul>\n" +
                     "\t\t\t\t\t                <li><a href=\"#\">Home</a></li>\n" +
                     "\t\t\t\t\t                <li><a href=\"#\">About</a></li>\n" +
                     "\t\t\t\t\t                <li><a href=\"#\">Services</a></li>\n" +
                     "\t\t\t\t\t                <li><a href=\"#\">Work</a></li>\n" +
                     "\t\t\t\t\t              </ul>\n" +
                     "                      </td>\n" +
                     "                    </tr>\n" +
                     "                  </table>\n" +
                     "                </td>\n" +
                     "              </tr>\n" +
                     "            </table>\n" +
                     "          </td>\n" +
                     "        </tr><!-- end: tr -->\n" +
                     "        <tr>\n" +
                     "        \t<td valign=\"middle\" class=\"bg_black footer email-section\">\n" +
                     "        \t\t<table>\n" +
                     "            \t<tr>\n" +
                     "                <td valign=\"top\" width=\"33.333%\">\n" +
                     "                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
                     "                    <tr>\n" +
                     "                      <td style=\"text-align: left; padding-right: 10px;\">\n" +
                     "                      \t<p>&copy; 2018 Corporate. All Rights Reserved</p>\n" +
                     "                      </td>\n" +
                     "                    </tr>\n" +
                     "                  </table>\n" +
                     "                </td>\n" +
                     "                <td valign=\"top\" width=\"33.333%\">\n" +
                     "                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
                     "                    <tr>\n" +
                     "                      <td style=\"text-align: right; padding-left: 5px; padding-right: 5px;\">\n" +
                     "                      \t<p><a href=\"#\" style=\"color: rgba(255,255,255,.4);\">Unsubcribe</a></p>\n" +
                     "                      </td>\n" +
                     "                    </tr>\n" +
                     "                  </table>\n" +
                     "                </td>\n" +
                     "              </tr>\n" +
                     "            </table>\n" +
                     "        \t</td>\n" +
                     "        </tr>\n" +
                     "      </table>\n" +
                     "\n" +
                     "    </div>\n" +
                     "  </center>\n" +
                     "</body>\n" +
                     "</html>";

   }

}
