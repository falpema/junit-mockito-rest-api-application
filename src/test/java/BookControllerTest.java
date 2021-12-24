import static org.hamcrest.CoreMatchers.is ;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rest.app.Book;
import com.rest.app.BookController;
import com.rest.app.BookRepository;

/**
 * class to do junit test and  Mockito
 * @author fabian.peñaloza
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {
     private MockMvc mockMvc;
     
     ObjectMapper objectMapper = new ObjectMapper();
     ObjectWriter objectWriter = objectMapper.writer();
     
     @Mock
     private BookRepository bookRepository ;
     
     @InjectMocks
     private BookController bookController;
     
     Book RECORD_1 = new Book( 1L, "Atomic Habits", "How to build better habits", 5);
     Book RECORD_2 = new Book( 2L, "Thinking Fast and Slow", "how to create good mentalist ", 4);
     Book RECORD_3 = new Book( 3L, "Grokking Algorithms", "creat a goot routine in the morning ", 4);
     
     @Before
      public void setUp() {
    	 MockitoAnnotations.initMocks(this);
    	 this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    	 
     }
     
     @Test
     public void getAllRecords_success() throws Exception {
    	 
    	 List<Book> records   =  new ArrayList<>() ;
    	 	records.add(RECORD_1);
    	 	records.add(RECORD_2);
    	 	records.add(RECORD_3);
    	 	Mockito.when(bookRepository.findAll()).thenReturn(records);
    	 	
    	 	mockMvc.perform(MockMvcRequestBuilders
    	 			.get("/book")
    	 			.contentType(MediaType.APPLICATION_JSON))
    	 			.andExpect(status().isOk())
    	 			.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
    	 			.andExpect(jsonPath("$[2].name" , is("Grokking Algorithms"))) ;
    	 	
     }
     
     @Test
     public void getBookByID_success() throws Exception {
    	 Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(java.util.Optional.of(RECORD_1));
    	 
  	 	mockMvc.perform(MockMvcRequestBuilders
	 			.get("/book/1")
	 			.contentType(MediaType.APPLICATION_JSON))
	 			.andExpect(status().isOk())
	 			.andExpect(jsonPath("$", notNullValue()))
	 			.andExpect(jsonPath("$.name" , is("Atomic Habits"))) ;
    	 
     }

     @Test
     public void createRecord_success() throws Exception {
    	 Book record = new Book();
    	 record.setBookId(5L);
    	 record.setName("Introduction to C");
    	 record.setRating(5);
    	 record.setSummary("The name but longer");
    	 
    	 Mockito.when(bookRepository.save(record)).thenReturn(record);
    	 String content = objectWriter.writeValueAsString(record);
    	 
    	 MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
    			 .contentType(MediaType.APPLICATION_JSON)
    			 .accept(MediaType.APPLICATION_JSON)
    			 .content(content);
    	 
    	 mockMvc.perform(mockRequest)
    	 .andExpect(status().isOk())
    	 .andExpect(jsonPath("$", notNullValue()))
    	 .andExpect(jsonPath("$.name", is("Introduction to C"))) ;
     }
     
     @Test
     public void updateBookRecord_success() throws Exception  {
    	 Book updatedRecord = new  Book();
    	 updatedRecord.setBookId(1L);
    	 updatedRecord.setName("Updated Book name");
    	 updatedRecord.setSummary("summary updated");
    	 updatedRecord.setRating(1);
    	 
    	 
    	 Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(java.util.Optional.ofNullable(updatedRecord));
    	 Mockito.when(bookRepository.save(updatedRecord)).thenReturn(updatedRecord);
    	 
    	 String updateContent = objectWriter.writeValueAsString(updatedRecord);
    	 MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
    			 .contentType(MediaType.APPLICATION_JSON)
    			 .accept(MediaType.APPLICATION_JSON)
    			 .content(updateContent);
    	 
    	 mockMvc.perform(mockRequest)
    	 	.andExpect(status().isOk())
    	 	.andExpect(jsonPath("$", notNullValue()))
    	 	.andExpect(jsonPath("$.name", is("Updated Book name")))
    	 ;
    	 
     }
     
     @Test
     public void deleteBookById_success() throws Exception {
    	 Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.of(RECORD_1));
    	 mockMvc.perform(MockMvcRequestBuilders
    			 .delete("/book/1")
    			 .contentType(MediaType.APPLICATION_JSON))
    	 		.andExpect(status().isOk())
    			 ;
    	 
     }
     

     
     
     
}
