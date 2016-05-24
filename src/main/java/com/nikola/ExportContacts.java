package com.nikola;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.StructuredName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 24/05/2016.
 */

@RestController
public class ExportContacts {

    private final ContactRepository contactRepository;
    private final AccountRepository accountRepository;

    @Autowired
    HttpServletRequest request;


    @Autowired
    public ExportContacts(ContactRepository contactRepository, AccountRepository accountRepository) {
        this.contactRepository = contactRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping("exportContact/{num}")
    public void exportContactByNumber(@PathVariable int num, HttpServletResponse response) {

        Account account = accountRepository.findByUsername(request.getSession().getAttribute("user").toString());

        List<Contact> contacts = contactRepository.findByAccountId(account.getId());

        VCard vCard = parseContact(contacts.get(num + 1));

        File file = new File("contacts.vcf");

        UiApplication.log.info(file.getAbsolutePath());

        try {
            Ezvcard.write(vCard).go(file);
            InputStream inputStream = new FileInputStream(file);

            response.setContentType("application/vcf");
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader("Content-Disposition", "attachment; filename=\"contact.vcf\"");

            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();

            UiApplication.log.info("Exported");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VCard parseContact(Contact contact) {
        VCard vcard = new VCard();
        StructuredName structuredName = new StructuredName();
        structuredName.setGiven(contact.getFirstName());
        structuredName.setFamily(contact.getLastName());

        vcard.setStructuredName(structuredName);
        vcard.addTelephoneNumber(contact.getPhoneNumber());

        return vcard;
    }

    @RequestMapping(value = "/exportAllContacts")
    public void exportAllContacts(HttpServletResponse response) {
        Account account = accountRepository.findByUsername(request.getSession().getAttribute("user").toString());

        List<Contact> contacts = contactRepository.findByAccountId(account.getId());
        ArrayList<VCard> vCards = new ArrayList<>();

        for (Contact contact : contacts) {
            vCards.add(parseContact(contact));
        }

        File file = new File("contacts.vcf");

        UiApplication.log.info(file.getAbsolutePath());

        try {
            Ezvcard.write(vCards).go(file);
            InputStream inputStream = new FileInputStream(file);

            response.setContentType("application/vcf");
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader("Content-Disposition", "attachment; filename=\"contacts.vcf\"");

            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();

            UiApplication.log.info("Exported");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
