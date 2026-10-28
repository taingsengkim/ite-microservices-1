package kh.edu.istad.platform.customer.client.persistence.converter;

import kh.edu.istad.platform.customer.client.domain.valueobject.Email;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;


@ReadingConverter
public class EmailReadConverter implements Converter<Document, Email> {
    @Override
    public Email convert(Document source) {
        return new Email(source.getString("email"));
    }
}
