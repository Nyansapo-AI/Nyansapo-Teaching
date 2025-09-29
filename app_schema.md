# Nyansapo AI Teaching Schema

## Collection Organization

```json
{
    "name" : "string",
    "admins" : ["string"],
    "teachers" : ["string"],
    "projects" : ["string"],
    "time_created": "timestamp"
}
```

## Subcollection: **Project** under Organization

```json
{ 
  "name": "string",                           
  "teachers": ["string"],    
  "schools": ["string"],
  "total_camps": "number",
  "total_students" : "number",
  "total_teachers" : "number",
  "total_schools" : "number",
  "sessions_completion_rate": "number",
  "teacher_to_student_ratio" : "number",
  "time_created" : "timestamps",
  "learning_level_distribution": [
    {
      "type": "string", //(literacy/numeracy) 
      "data" : [
        {
          "grade": "number",
          "distribution" : [
            {
              "learning_level": "string",
              "value" : "float"
            }
          ]
        }
      ]
    }
  ]     
} 
```

## Subcollection: **Schools** under Project

```json
{ 

  "name": "string",                       
  "admins": ["string"],               
  "teachers": ["string"],                    
  "camps": ["string"],                      
  "total_camps": "number",             
  "total_teachers": "number",            
  "total_students": "number"             

}
```

## Subcollection: Camps under schools

```json
{ 
  "name": "string",                           
  "teachers": ["string"],  
  "students" : ["string"],                  
  "attendanceStats": {                  
    "present": "number", 
    "absent": "number", 
    "atRisk": "number"
  }, 
  "attendance_rate": "number",

  "assessments": ["string"],

  "total_students": "number",         

  "total_teachers": "number" ,

  "learning_level_distribution": [
    {
      "type": "string", //(literacy/numeracy) 
      "data" : [
        {
          "grade": "number",
          "distribution" : [
            {
              "learning_level": "string",
              "value" : "float"
            }
          ]
        }
      ]
    }
  ]

} 
```

## Subcollection: Attendance under Schools

```json
{
    "attendance": [
        {
          "createdAt": "timestamp",                      
        
          "date": "string",                              
        
          "students": [ 
        
                { 
            
                    "id": "string", 
            
                    "name": "string", 
            
                    "attendance": "boolean"
            
                } 
        
          ]
        } 

    ] 
}
```

## Subcollection: Assessments under Camps

```json
{ 

  "name": "string",                            

  "created_at": "timestamp",           
  
  "assessmentNumber": "number",

  "type": "string", 

  "start_level": ["string"], 

  "alert": { 
    "day_before_assessment": "number", 
  }, 
  
  "level_distribution": [
    {
      "type": "string", //(literacy/numeracy) 
      "data" : [
        {
          "learning_level": "string",
          "value" : "float"
        }
      ]
    }
  ],
  
  "assigned_students": [                      
    
        { 
    
          "student_id": "string", 

          "student_name": "string",
    
          "competence": "number?" 
    
        } 
    
  ]

 

} 
```

## Subcollection Assessment results under assessments

```json
{ 
  "completed_assessment": "boolean", 
  "student_id": "uid",          
  "competence_level": "number", 
  "literacy_results": {
  "reading_results": [
    { 
    
        "type": "string", // letter/word/paragraph/story
        
        "content": "string", 
        
        "metadata" : {
        
            "audio_url": "string", 
            
            "passed" : "boolean", 
            
            "transcript": "string"
        
        }
    
    } 

], 

"multiple_choices_results" : [ 

    { 
    
    "question": "string", 
    
    "student_answer": "string", 

    "passed" : "boolean",
    
    "options" : [ "string"] //size 3
    
    }
],

"numeracy_results" : { 

    "count_and_match": [ 
    
        { 
        
          "expected_number": "number", 
          
          "student_answer": "number", 
          
          "passed": "boolean" 
        
        } 
    
    ], 

    "number_recognition" : [
    
        {
            "type": "string", //number_recognition 
            
            "content": "string", 
            
            "metadata" : { 
            
                "audio_url": "string", 
                
                "passed" : "boolean", 
                
                "transcript": "number"
            }
        }
  
    ], 

 

    "number_operations" : [ 
    
        { 
        
            "type": "string",  // (addtion/subtraction/multiplication/division) 
            
            "expected_answer": "number", 
            
            "student_answer": "number", 
            
            "operations_number1": "number", 
            
            "operations_number2" : "number", 
            
            "metadata" : {  
            
                "screenshot_url": "string", 
                
                "passed" : "boolean", 
                
                "transcript": "number"
            
            }
        } 
    ], 

    "word_problem" : [ { 
    
        "question" : "string", 
        
        "expected_number": "number", 
        
        "metadata" : { 
        
            "screenshot_url": "string", 
            
            "passed": "boolean", 
            
            "transcript": "number"
        
        } 
    }
    ]   
    } 

}


} 
```

## Collection User

```json

{
  "uid": "string",                      

  "name": "string",                          

  "email": "string",                         

  "phone": "string",                         

  "class": "string",                         

  "createdAt": "string",                     

  "lastUpdated": "timestamp",

  "organizations": [
    {
      "name": "string",
      "id": "string",
      "projects": [
        {
          "name": "string",
          "id" : "string",
          "is_manager" : "boolean",
          "schools" : [
            {
              "name" : "string",
              "id" : "string",
              "camps" : [
                {
                  "name" : "string",
                  "id" : "string"
                }
              ]
            }
          ]
        }
      ]
    }
  ],

} 
```
