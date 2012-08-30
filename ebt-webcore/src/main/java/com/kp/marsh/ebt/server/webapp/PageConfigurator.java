/**
 * 
 */
package com.kp.marsh.ebt.server.webapp;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.dozer.DozerBeanMapperSingletonWrapper;

import com.google.inject.Singleton;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;

/**
 * @author christianachilli
 * Injected at startup to configure lobs and products by page. Such configuration does not change until the DB changes, so it may be singleton
 */
@Singleton
public class PageConfigurator {

    private static int MAX_COL_NUMBER = 8; // NUMBER OF COLUMNS OF THE VISIBLE TABLE

    Logger log = Logger.getLogger(PageConfigurator.class);

    private DomainDrillerService domainDrillerService;

    /**
     * Lists the LOBs contained in each table listed in <code>headlineTableList</code>
     */
    ArrayList<ArrayList<LineOfBusiness>> lobsByPageList; // LOB list split per display page 

    public PageConfigurator(DomainDrillerService domainDriller) {
        this.domainDrillerService = domainDriller;
        splitBusinessLineByPage();
    }

    /**
     * Main algorithm 
     */
    private void splitBusinessLineByPage() {

        List<LineOfBusiness> lobsList = getLOBList();

        lobsByPageList = new ArrayList<ArrayList<LineOfBusiness>>();
        ArrayList<LineOfBusiness> lobsInCurrentPage = new ArrayList<LineOfBusiness>();

        // Each table may contain up to 8 column 75 px width each.

        int lobConsumed = 0;
        int numbOfPrd = 0;
        int consumedColumn = 0; // used to check if some spacer padding is needed in the last page

        for (LineOfBusiness lob : lobsList) {

            lobConsumed++;
            numbOfPrd += lob.getProducts().size();
            consumedColumn += lob.getProducts().size();
            if (numbOfPrd + (lobConsumed - 1) <= MAX_COL_NUMBER) {// (lobConsumed - 1)is the number of needed spacer: if the number of max columns has not been reached
                if (lobConsumed > 1) { // if it's not the first one lob, a spacer must be added before the next lob
                    consumedColumn++;

                }
            } else { // create a new table

                lobsByPageList.add(lobsInCurrentPage); // adding the actual page list
                lobsInCurrentPage = new ArrayList<LineOfBusiness>(); // instantiate the new page list
                numbOfPrd = lob.getProducts().size();
                lobConsumed = 1;
                consumedColumn = numbOfPrd = lob.getProducts().size();
            }

            // add the lob
            lobsInCurrentPage.add(lob);

        }

        lobsByPageList.add(lobsInCurrentPage); // adding the last page list

    }

    private void convertHtmlDescription(ProductInfoDTO buff) {
        // with Velocity create the html template for the product label pop up
        VelocityEngine ve = new VelocityEngine();
        Properties prop = new Properties();

        prop.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getCanonicalName());
        prop.setProperty("css.src.dir", ClasspathResourceLoader.class.getCanonicalName()
                + "/com/kp/marsh/ebt/client/ui/resources/css");

        ve.init(prop);
        /*  next, get the Template  */
        Template t = ve.getTemplate("/productDescription.vm");
        /*  create a context and add data */

        VelocityContext context = new VelocityContext();

        final String separator = ";;!!;;";
        String formattedString = buff.getNotes();
        String[] separatedArray = StringUtils.split(formattedString, separator);

        try {
            context.put("nomeProdotto", separatedArray[0]);
            context.put("descrizione", separatedArray[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.warn("Descrizione prodotto non formattata correttamente: " + formattedString);
        }
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        String html = writer.toString();
        buff.setNotes(html);
        //
    }

    private List<LineOfBusiness> getLOBList() {

        List<Products> lobs = domainDrillerService.getAvailableLOB();

        //elaboro la lista dei DTO per discernere prodotti, lob e associarli tra loro

        List<LineOfBusiness> lobsDTOs = new ArrayList<LineOfBusiness>(lobs != null ? lobs.size() : 0);

        if (lobsDTOs != null) {
            for (Products product : lobs) {

                ProductInfoDTO lobBuf = DozerBeanMapperSingletonWrapper.getInstance()
                        .map(product, ProductInfoDTO.class);

                LineOfBusiness lobTemp = new LineOfBusiness(lobBuf);

                // retrieve products of the lob
                List<Products> lobProducts = domainDrillerService.getAvailableProductsByLOB(product);

                List<ProductInfoDTO> productsDTOs = new ArrayList<ProductInfoDTO>(
                        lobProducts != null ? lobProducts.size() : 0);

                for (Products products : lobProducts) {
                    ProductInfoDTO buff = DozerBeanMapperSingletonWrapper.getInstance().map(products,
                            ProductInfoDTO.class);
                    productsDTOs.add(buff);
                    convertHtmlDescription(buff);
                }

                lobTemp.setProducts(productsDTOs);

                lobsDTOs.add(lobTemp);
            }
        }

        return lobsDTOs;
    }

}
